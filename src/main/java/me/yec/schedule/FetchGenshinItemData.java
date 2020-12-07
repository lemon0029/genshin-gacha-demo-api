package me.yec.schedule;

import lombok.extern.slf4j.Slf4j;
import me.yec.config.MihoyoProperties;
import me.yec.model.entity.item.GenshinCharacter;
import me.yec.model.entity.item.GenshinItemType;
import me.yec.model.entity.item.GenshinWeapon;
import me.yec.repository.GenshinCharacterRepository;
import me.yec.repository.GenshinWeaponRepository;
import me.yec.util.Requests;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 定时获取原神角色/武器信息
 *
 * @author yec
 * @date 12/4/20 8:10 PM
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class FetchGenshinItemData {

    private static final String BASE_URL = "https://api-takumi.mihoyo.com/event/e20200928calculate/v1";
    private final MihoyoProperties mihoyoProperties;
    private final GenshinCharacterRepository genshinCharacterRepository;
    private final GenshinWeaponRepository genshinWeaponRepository;

    public FetchGenshinItemData(MihoyoProperties mihoyoProperties,
                                GenshinCharacterRepository genshinCharacterRepository,
                                GenshinWeaponRepository genshinWeaponRepository) {
        this.mihoyoProperties = mihoyoProperties;
        this.genshinCharacterRepository = genshinCharacterRepository;
        this.genshinWeaponRepository = genshinWeaponRepository;

        // 初始化图片保存路径
        initDir(mihoyoProperties.getImgSaveDir());
    }

    /**
     * 定时抓取原神的角色信息
     */
    @Async
    @Scheduled(initialDelay = 3600000, fixedDelay = 864000000)
    public void fetchCharacters() {
        fetchData(GenshinItemType.CHARACTER);
    }

    /**
     * 定时抓取原神的武器信息
     */
    @Async
    @Scheduled(initialDelay = 3600000, fixedDelay = 864000000)
    public void fetWeapons() {
        fetchData(GenshinItemType.WEAPON);
    }

    /**
     * 初始化拼接 cookie （作为请求头）
     *
     * @return 请求头
     */
    private Header initHeaderOfCookie() {
        String accountId = mihoyoProperties.getAccountId();
        String cookieToken = mihoyoProperties.getCookieToken();
        return new BasicHeader("cookie", String.format("account_id=%s;cookie_token=%s", accountId, cookieToken));
    }

    /**
     * 从网络 URL 保存角色头像到文件夹中
     * 暂时来说角色的网络链接和武器的网络链接路径格式一致通用。
     *
     * @param url 图片外链
     * @param dir 保存路径
     * @return 文件名
     */
    private String saveImageToDirFromUrl(String url, String dir) {
        /*
         * 外链类似：
         * https://uploadstatic.mihoyo.com/hk4e/e20200928calculate/item_icon_745c4j/56e74fae596e40b20d65a666ee5889ed.png
         * https://uploadstatic.mihoyo.com/hk4e/e20200928calculate/item_icon_745c4j/49d06da043a93d0b7abe0833f729152a.png
         */
        String imageName = url.split("item_icon_.*/")[1];
        byte[] bytes = Requests.getBytes(url);
        File file = new File(dir + imageName);
        saveImageToFileFromBytes(file, bytes);
        return imageName;
    }

    /**
     * 初始化图片保存路径
     *
     * @param url 目录
     */
    private void initDir(String url) {
        File file = new File(url);
        if (!file.exists()) {
            boolean mkdir = file.mkdirs();
            if (mkdir) {
                log.info("create directory[{}] success!", url);
            } else {
                log.info("create directory[{}] failed!", url);
            }
        } else {
            log.info("img directory[{}] is existed", url);
        }
    }

    /**
     * 保存图片（字节）到文件中
     *
     * @param file  指定保存文件
     * @param bytes 字节
     */
    private void saveImageToFileFromBytes(File file, byte[] bytes) {
        if (!file.exists()) {
            FileOutputStream outputStream = null;
            try {
                // 如果该文件不存在则创建新文件（待完善）
                boolean newFile = file.createNewFile(); // createNewFile 方法会抛出 IO 异常
                if (newFile) {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(bytes);
                    outputStream.flush();
                    log.info("save picture[{}] success", file);
                }
            } catch (IOException e) {
                log.error("create new file[{}] failed!", file.getAbsolutePath());
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("picture[{}] is existed", file);
        }
    }

    /**
     * 武器和角色数据高度相似，采用一个方法将数据保存到数据库
     *
     * @param dataList 数据列表
     * @param type     武器/角色类型
     */
    private void saveGenshinItem(JSONArray dataList, GenshinItemType type) {
        for (int i = 0; i < dataList.length(); i++) {
            // 获取 JSONObject
            JSONObject item = dataList.optJSONObject(i);
            // 从 JSONObject 中获取数据，为做数据真实性检验
            long id = item.optLong("id");
            String name = item.optString("name");
            String icon = item.optString("icon");
            String imgSaveDir = mihoyoProperties.getImgSaveDir();
            if (!imgSaveDir.endsWith("/")) imgSaveDir += "/";
            String saveImageName = saveImageToDirFromUrl(icon, imgSaveDir);
            icon = "img/" + saveImageName;

            if (type == GenshinItemType.CHARACTER) {
                int avatarLevel = item.optInt("avatar_level");
                int elementAttrId = item.optInt("element_attr_id");
                // 创建实体类对象并设置数据
                GenshinCharacter genshinCharacter = new GenshinCharacter();
                genshinCharacter.setId(id);
                genshinCharacter.setName(name);
                genshinCharacter.setAvatar(icon);
                genshinCharacter.setCharacterAttrId(elementAttrId);
                genshinCharacter.setRanting(avatarLevel);
                // 保存到数据库
                genshinCharacterRepository.save(genshinCharacter);
            } else {
                int weaponLevel = item.optInt("weapon_level");
                // 创建实体类对象并设置数据
                GenshinWeapon genshinWeapon = new GenshinWeapon();
                genshinWeapon.setId(id);
                genshinWeapon.setName(name);
                genshinWeapon.setAvatar(icon);
                genshinWeapon.setRanting(weaponLevel);
                // 保存到数据库
                genshinWeaponRepository.save(genshinWeapon);
            }
        }
    }

    public void fetchData(GenshinItemType type) {
        String url;
        if (type == GenshinItemType.CHARACTER) {
            url = BASE_URL + "/avatar/list";
        } else {
            url = BASE_URL + "/weapon/list";
        }

        String data = "{\"page\": 1, \"size\": 100}";

        // post 请求的 body 未作非空判断
        String body = Requests.post(url, initHeaderOfCookie(), data);
        // jsonBody 为空则说明转换异常
        JSONObject jsonBody = Requests.parseOf(body);

        if (jsonBody != null) {
            // 判断响应内容是否符合预期结果，可能出现 cookie 失效提示需要登录的结果
            if (Requests.respIsError(jsonBody)) {
                log.warn("response body is not expected");
            } else {
                // 获取具体的数据列表，如果有异常情况 dataList 将为 null
                JSONArray dataList = jsonBody.optJSONObject("data").optJSONArray("list");
                if (type == GenshinItemType.CHARACTER) {
                    saveGenshinItem(dataList, GenshinItemType.CHARACTER); // 保存到数据库
                } else {
                    saveGenshinItem(dataList, GenshinItemType.WEAPON); // 保存到数据库
                }
            }
        }

        if (type == GenshinItemType.CHARACTER) {
            log.info("fetch or update character data success");
        } else {
            log.info("fetch or update weapon data success");
        }
    }
}
