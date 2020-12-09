package me.yec.schedule;

import lombok.extern.slf4j.Slf4j;
import me.yec.model.entity.gacha.GenshinGachaPool;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.repository.GenshinGachaPoolItemRepository;
import me.yec.repository.GenshinGachaPoolRepository;
import me.yec.util.Requests;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 定时获取原神抽奖池信息
 *
 * @author yec
 * @date 12/4/20 8:10 PM
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class FetchGenshinGachaPoolData {

    private static final String BASE_URL = "https://webstatic.mihoyo.com/hk4e/gacha_info/cn_gf01";

    private final GenshinGachaPoolRepository genshinGachaPoolRepository;
    private final GenshinGachaPoolInfoRepository genshinGachaPoolInfoRepository;
    private final GenshinGachaPoolItemRepository genshinGachaPoolItemRepository;

    public FetchGenshinGachaPoolData(GenshinGachaPoolRepository genshinGachaPoolRepository,
                                     GenshinGachaPoolInfoRepository genshinGachaPoolInfoRepository,
                                     GenshinGachaPoolItemRepository genshinGachaPoolItemRepository) {
        this.genshinGachaPoolRepository = genshinGachaPoolRepository;
        this.genshinGachaPoolInfoRepository = genshinGachaPoolInfoRepository;
        this.genshinGachaPoolItemRepository = genshinGachaPoolItemRepository;
    }

    /**
     * 定时更新池子列表，官方这个接口只有三个正在抽奖的池子，其它的得另外想办法...
     * 首次运行延时 1 小时后执行（本地没资源的话建议启动就执行，置为0即可）
     */
    @Async
    @Scheduled(initialDelay = 3600000, fixedDelay = 864000000)
    public void fetchGachaList() {
        String url = BASE_URL + "/gacha/list.json";

        String s = Requests.get(url);
        JSONObject jsonBody = Requests.parseOf(s);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (jsonBody != null) {
            // 判断响应内容是否符合预期结果，可能出现 cookie 失效提示需要登录的结果
            if (Requests.respIsError(jsonBody)) {
                log.warn("response body is not expected");
            } else {
                // 获取具体的数据列表，如果有异常情况 dataList 将为 null
                JSONArray dataList = jsonBody.optJSONObject("data").optJSONArray("list");
                for (int i = 0; i < dataList.length(); i++) {
                    JSONObject jsonObject = dataList.optJSONObject(i);
                    String gachaId = jsonObject.optString("gacha_id");
                    String gachaName = jsonObject.optString("gacha_name");
                    Integer gachaType = jsonObject.optInt("gacha_type");
                    String beginTime = jsonObject.optString("begin_time");
                    String endTime = jsonObject.optString("end_time");

                    GenshinGachaPool genshinGachaPool = new GenshinGachaPool();
                    genshinGachaPool.setId(gachaId);
                    genshinGachaPool.setName(gachaName);
                    genshinGachaPool.setType(gachaType);
                    genshinGachaPool.setBeginTime(LocalDateTime.parse(beginTime, dateTimeFormatter));
                    genshinGachaPool.setEndTime(LocalDateTime.parse(endTime, dateTimeFormatter));
                    genshinGachaPoolRepository.save(genshinGachaPool);
                }
                log.info("update gacha pool success");
            }
        }
    }

    /**
     * 定时抓取池子的信息，一般来说半个月更新一次就好了
     */
    @Async
    @Scheduled(initialDelay = 3600000, fixedDelay = 864000000)
    public void fetchGachaPoolInfo() {
        List<GenshinGachaPool> gachaPools = genshinGachaPoolRepository.findAll();
        if (gachaPools.size() == 0) log.info("nothing gacha pool info can updatable");
        gachaPools.forEach(genshinGachaPool -> {
            // 发起HTTP请求拿到数据
            String url = BASE_URL + "/" + genshinGachaPool.getId() + "/zh-cn.json";
            String s = Requests.get(url);
            JSONObject jsonBody = Requests.parseOf(s);

            if (jsonBody != null) {
                // 提取数据保存池子信息
                GenshinGachaPoolInfo gachaPoolInfo = new GenshinGachaPoolInfo();
                gachaPoolInfo.setId(genshinGachaPool.getId());
                gachaPoolInfo.setGachaId(jsonBody.optInt("gacha_id"));

                String title = jsonBody.optString("title");
                gachaPoolInfo.setTitle(title);
                gachaPoolInfo.setNickTitle(title);
                gachaPoolInfo.setContent(jsonBody.optString("content"));
                gachaPoolInfo.setGachaType(jsonBody.optInt("gacha_type"));

                String r5UpProb = jsonBody.optString("r5_up_prob");
                gachaPoolInfo.setR5UpProb(strPercentToDouble(r5UpProb));

                String r4UpProb = jsonBody.optString("r4_up_prob");
                gachaPoolInfo.setR4UpProb(strPercentToDouble(r4UpProb));

                String r5Prob = jsonBody.optString("r5_prob");
                gachaPoolInfo.setR5Prob(strPercentToDouble(r5Prob));

                String r4Prob = jsonBody.optString("r4_prob");
                gachaPoolInfo.setR4Prob(strPercentToDouble(r4Prob));

                String r3Prob = jsonBody.optString("r3_prob");
                gachaPoolInfo.setR3Prob(strPercentToDouble(r3Prob));

                String r5BaodiProb = jsonBody.optString("r5_baodi_prob");
                gachaPoolInfo.setR5BaodiProb(strPercentToDouble(r5BaodiProb));

                String r4BaodiProb = jsonBody.optString("r4_baodi_prob");
                gachaPoolInfo.setR4BaodiProb(strPercentToDouble(r4BaodiProb));

                String r3BaodiProb = jsonBody.optString("r3_baodi_prob");
                gachaPoolInfo.setR3BaodiProb(strPercentToDouble(r3BaodiProb));

                GenshinGachaPoolInfo save = genshinGachaPoolInfoRepository.save(gachaPoolInfo);
                log.info("update gacha pool info<{}> success", save.getId());

                JSONArray r5ProbList = jsonBody.optJSONArray("r5_prob_list");
                JSONArray r4ProbList = jsonBody.optJSONArray("r4_prob_list");
                JSONArray r3ProbList = jsonBody.optJSONArray("r3_prob_list");

                saveGachaItem(r5ProbList, 5);
                saveGachaItem(r4ProbList, 4);
                saveGachaItem(r3ProbList, 3);
            }
        });
    }

    /**
     * 将百分比字符串转成浮点数，如：
     * 7.5% -> 7.5 （由于浮点数精度问题，就不除一百了，最后总数还是100）
     * 还是除一百吧...
     * 7.5% -> 0.075
     *
     * @param s 字符串
     * @return 浮点类型的的百分数（总数为100）
     */
    private Double strPercentToDouble(String s) {
        return Double.parseDouble(s.replace("%", "")) / 100;
    }

    /**
     * 通过给定参数保存 GachaPoolItem 对象到数据库
     *
     * @param jsonArray JSONArray 类型的数据
     * @param ranting   指定对象的星级（便于后期取数据做概率计算）
     */
    private void saveGachaItem(JSONArray jsonArray, int ranting) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            GenshinGachaPoolItem gachaPoolItem = new GenshinGachaPoolItem();

            gachaPoolItem.setGachaId(jsonObject.optInt("gacha_id"));
            gachaPoolItem.setUp(jsonObject.optInt("is_up") == 1);

            // Venti 池的概率数据显示不一样...不过一般不用在乎...
            String probStr;
            if (jsonObject.optJSONObject("prob") == null)
                probStr = jsonObject.optString("prob");
            else
                probStr = jsonObject.optJSONObject("prob").optString("prob");
            gachaPoolItem.setProb(strPercentToDouble(probStr));

            gachaPoolItem.setName(jsonObject.optString("item_name"));
            gachaPoolItem.setType(jsonObject.optString("item_type"));

            // 数据源的 itemId 位数比较少，类似与：1003 --> 10000003
            String itemId = jsonObject.optString("item_id");
            if ("角色".equals(gachaPoolItem.getType())) {
                String substring = itemId.substring(0, 1);
                String substring1 = itemId.substring(1);
                gachaPoolItem.setItemId(Long.valueOf(substring + "0000" + substring1));
            } else {
                gachaPoolItem.setItemId(Long.valueOf(itemId));
            }

            gachaPoolItem.setRanting(ranting);

            /* 由于多个池子包含同样的角色，所以使用子增长 ID
                而 Spring data jap 在不指定 id 的时候执行 save 方法就是插入
                因此首先要判断是否包含当前数据再做更新（或插入）
             */
            Example<GenshinGachaPoolItem> itemExample = Example.of(gachaPoolItem);
            Optional<GenshinGachaPoolItem> one = genshinGachaPoolItemRepository.findOne(itemExample);
            if (one.isEmpty()) {
                GenshinGachaPoolItem gachaPoolItemSave = genshinGachaPoolItemRepository.save(gachaPoolItem);
                log.info("save gacha pool item [{}] success", gachaPoolItemSave);
            }
        }
    }


}
