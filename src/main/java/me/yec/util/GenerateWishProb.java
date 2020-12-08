package me.yec.util;

import lombok.extern.slf4j.Slf4j;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化奖池概率
 *
 * @author yec
 * @date 12/8/20 1:36 PM
 */
@Slf4j
public class GenerateWishProb {

    private static final Map<String, Map<String, Number>> probMap = new HashMap<>();

    /**
     * 获取抽奖概率，其实这里获取的概率是官网上的，但是还有一个单独物品概率清单，那又是另外一回事了...
     *
     * @param gachaPoolInfo 池子信息
     * @return 池子各种概率
     */
    public static Map<String, Number> getProbBy(GenshinGachaPoolInfo gachaPoolInfo) {
        if (probMap.get(gachaPoolInfo.getId()) == null) {
            generateWishPoolProb(gachaPoolInfo);
        }
        return probMap.get(gachaPoolInfo.getId());
    }

    /**
     * 项目启动初始化所有池子的概率
     * 同时根据池子的类型设定保底
     */
    private static void generateWishPoolProb(GenshinGachaPoolInfo gachaPoolInfo) {
        HashMap<String, Number> stringDoubleHashMap = new HashMap<>();
        stringDoubleHashMap.put("r3Prob", gachaPoolInfo.getR3Prob() / 100);
        stringDoubleHashMap.put("r4Prob", gachaPoolInfo.getR4Prob() / 100);
        stringDoubleHashMap.put("r5Prob", gachaPoolInfo.getR5Prob() / 100);
        stringDoubleHashMap.put("r4UpProb", gachaPoolInfo.getR4UpProb() / 100);
        stringDoubleHashMap.put("r5UpProb", gachaPoolInfo.getR5UpProb() / 100);

        stringDoubleHashMap.put("c4Baodi", 10);
        stringDoubleHashMap.put("c5Baodi", 90);

        // 如果是武器池，则保底策略不一样
        if (gachaPoolInfo.getGachaType() == 302) {
            stringDoubleHashMap.put("c5Baodi", 80);
        }

        probMap.put(gachaPoolInfo.getId(), stringDoubleHashMap);

        log.info("Generate wish pool probability success");
    }


}
