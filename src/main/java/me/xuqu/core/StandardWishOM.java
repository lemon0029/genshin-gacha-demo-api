package me.xuqu.core;

import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;
import me.xuqu.model.support.wishpool.StandardPool;
import me.xuqu.util.WishOMUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 常驻池抽奖
 * 参照某社区大佬的代码，这里做了一个软保底，最后得到的概率：
 * 5星接近 1.6%，四星接近 13%
 *
 * @author yec
 * @date 12/7/20 3:11 PM
 */
public class StandardWishOM {

    // 软保底
    private final static double PROB4UP = 0.511; // 第 8 或 9 次四星概率提升
    private final static double PROB5UP = 0.324; // 第 75 到 89 次之间五星概率提升
    private final static int _BAODI4THRESHOLD = 8; // 4 星概率提升抽取次数阀门
    private final static int _BAODI5THRESHOLD = 75; // 5 星概率提升抽取次数阀门

    /**
     * 常驻池祈愿
     *
     * @param mapProb 奖池相关概率
     * @param lotteryUser 抽奖用户
     * @param items 池中的奖励
     * @param n     抽奖次数
     */
    public static List<Long> wish(Map<String, Number> mapProb,
                                  LotteryUser lotteryUser,
                                  String poolId,
                                  List<GenshinGachaPoolItem> items,
                                  int n) {
        StandardPool pool = (StandardPool) lotteryUser.getPoolById(poolId);

        // 获取抽奖概率
        Double r4prob = (Double) mapProb.get("r4Prob");
        Double r5prob = (Double) mapProb.get("r5Prob");
        Integer c4Baodi = (Integer) mapProb.get("c4Baodi");
        Integer c5Baodi = (Integer) mapProb.get("c5Baodi");

        // 不知道静态方法里 new 的对象多久回收...
        SecureRandom secureRandom = new SecureRandom();

        List<Long> tempItemIds = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            boolean get4Star = false; // 默认此次抽奖未获得4星
            boolean get5Star = false; // 默认此次抽奖未获得5星
            double v = secureRandom.nextDouble();

            if (pool.lastGet5Count < _BAODI5THRESHOLD) { // 1 - 89 发
                if (v < r5prob) get5Star = true;
                else {
                    if (pool.lastGet4Count < _BAODI4THRESHOLD) {
                        if (v < r4prob + r5prob) get4Star = true;
                    } else if (pool.lastGet4Count < c4Baodi - 1) {
                        if (v < PROB4UP + r5prob) get4Star = true;
                    } else {
                        get4Star = true;
                    }
                }
            } else if (pool.lastGet5Count < r5prob - 1) {
                if (v < PROB5UP) get5Star = true;
                else {
                    if (pool.lastGet4Count < _BAODI4THRESHOLD) {
                        if (v < r4prob + PROB5UP) get4Star = true;
                    } else if (pool.lastGet4Count < c4Baodi - 1) {
                        if (v < PROB4UP + PROB5UP) get4Star = true;
                    } else {
                        get4Star = true;
                    }
                }
            } else {
                get5Star = true;
            }

            if (get5Star) {
                if (pool.lastGet5Count == c5Baodi - 1) pool.get5ByBaodiCount++;
                pool.lastGet4Count++;
                pool.lastGet5Count = 0;
                pool.totalGet5Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 5));
            } else if (get4Star) {
                if (pool.lastGet4Count == c4Baodi - 1) pool.get4ByBaodiCount++;
                pool.lastGet4Count = 0;
                pool.lastGet5Count++;
                pool.totalGet4Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 4));
            } else {
                pool.lastGet4Count++;
                pool.lastGet5Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 3));
            }
        }

        pool.wishInventory.addAll(tempItemIds);
        pool.totalWishCount += n;
        return tempItemIds;
    }

}
