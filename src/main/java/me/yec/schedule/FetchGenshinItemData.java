package me.yec.schedule;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FetchGenshinItemData {

    private static final String BASE_URL = "https://api-takumi.mihoyo.com/event/e20200928calculate/v1";
    private final MihoyoProperties mihoyoProperties;
    private final GenshinCharacterRepository genshinCharacterRepository;
    private final GenshinWeaponRepository genshinWeaponRepository;

    /**
     * 定时抓取原神的角色信息
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 3, initialDelay = 500000)
    public void fetchCharacters() {
        fetchData(GenshinItemType.CHARACTER);
    }

    /**
     * 定时抓取原神的武器信息
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 3, initialDelay = 500000)
    public void fetWeapons() {
        fetchData(GenshinItemType.WEAPON);
    }

    /**
     * 角色属性（名称）没有太大的必要...就不实现了
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 3, initialDelay = 50000000)
    public void fetchCharacterAttributes() {
//        String url = BASE_URL + "/item/filter";
    }

    /**
     * 初始化拼接 cookie （作为请求头）
     *
     * @return 请求头
     */
    private Header getHeaderOfCookie() {
        String accountId = mihoyoProperties.getAccountId();
        String cookieToken = mihoyoProperties.getCookieToken();
        return new BasicHeader("cookie", String.format("account_id=%s;cookie_token=%s", accountId, cookieToken));
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
        String body = Requests.post(url, getHeaderOfCookie(), data);
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
