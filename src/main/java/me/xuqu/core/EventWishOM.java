package me.xuqu.core;

import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;
import me.xuqu.model.support.wishpool.GenshinEventWishPool;
import me.xuqu.model.support.wishpool.GenshinWishPoolType;
import me.xuqu.util.WishOMUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 活动池抽奖...
 * 角色活动池还是有一个软保底...
 * 武器活动池纯根据官方给定的概率计算
 *
 * @author yec
 * @date 12/7/20 3:14 PM
 */
@Component
public class EventWishOM {
    // 软保底
    private final static double PROB4UP = 0.511; // 第 8 或 9 次四星概率提升
    private final static double PROB5UP = 0.324; // 第 75 到 89 次之间五星概率提升
    private final static int _BAODI4THRESHOLD = 8; // 4 星概率提升抽取次数阀门
    private final static int _BAODI5THRESHOLD = 75; // 5 星概率提升抽取次数阀门

    private final static SecureRandom secureRandom = new SecureRandom();

    /**
     * 角色活动池抽奖
     *
     * @param poolId 抽奖池ID
     * @param items  抽奖物品
     * @param n      抽奖次数
     * @return 奖品ID
     */
    public static List<Long> wish(Map<String, Number> mapProb,
                                  LotteryUser lotteryUser,
                                  String poolId,
                                  List<GenshinGachaPoolItem> items,
                                  int n) {

        GenshinEventWishPool pool = lotteryUser.getEventPool(poolId);

        // 获取抽奖概率
        Double r4prob = (Double) mapProb.get("r4Prob");
        Double r5prob = (Double) mapProb.get("r5Prob");
        Integer c4Baodi = (Integer) mapProb.get("c4Baodi");
        Integer c5Baodi = (Integer) mapProb.get("c5Baodi");
        Double r4UpProb = (Double) mapProb.get("r4UpProb");
        Double r5UpProb = (Double) mapProb.get("r5UpProb");

        List<Long> tempItemIds = new ArrayList<>();

        if (pool.type == GenshinWishPoolType.WEAPON_EVENT) {
            for (int i = 0; i < n; i++) {
                double v = secureRandom.nextDouble();
                boolean get4Star = false; // 默认此次抽奖未获得4星
                boolean get5Star = false; // 默认此次抽奖未获得5星

                // 不做软保底操作
                if (pool.lastGet5Count < c5Baodi - 1) {
                    if (v < r5prob) get5Star = true;
                    else {
                        if (pool.lastGet4Count < c4Baodi - 1) {
                            if (v < r4prob) get4Star = true;
                        } else {
                            get4Star = true;
                        }
                    }
                } else {
                    get5Star = true;
                }

                addGift(items, pool, c4Baodi, c5Baodi, r4UpProb, r5UpProb, tempItemIds, get4Star, get5Star);
            }
        } else {
            for (int i = 0; i < n; i++) {
                double v = secureRandom.nextDouble();
                boolean get4Star = false; // 默认此次抽奖未获得4星
                boolean get5Star = false; // 默认此次抽奖未获得5星

                if (pool.lastGet5Count < _BAODI5THRESHOLD) {
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

                addGift(items, pool, c4Baodi, c5Baodi, r4UpProb, r5UpProb, tempItemIds, get4Star, get5Star);
            }
        }
        pool.wishInventory.addAll(tempItemIds);
        pool.totalWishCount += n;
        return tempItemIds;
    }

    private static void addGift(List<GenshinGachaPoolItem> items, GenshinEventWishPool pool, Integer c4Baodi, Integer c5Baodi, Double r4UpProb, Double r5UpProb, List<Long> tempItemIds, boolean get4Star, boolean get5Star) {
        if (get5Star) { // 五星物品
            if (pool.lastGet5Count == c5Baodi - 1) pool.get5ByBaodiCount++; // 五星保底了
            pool.lastGet5Count = 0; // 上一次获得五星计数清零
            pool.lastGet4Count++;
            pool.totalGet5Count++;
            if (!pool.lastGet5isUp) {  // 如果上一次5星不是UP角色
                tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, true));
                pool.lastGet5isUp = true; // 最后获得五星是
                pool.totalGet5UpCount++;
            } else {
                if (secureRandom.nextDouble() < r5UpProb) { // 选择是否为UP
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, true));
                    pool.totalGet5UpCount++;
                    pool.lastGet5isUp = true;
                } else {
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, false));
                    pool.lastGet5isUp = false;
                }
            }
        } else if (get4Star) { // 四星物品
            if (pool.lastGet4Count == c4Baodi - 1) pool.get4ByBaodiCount++; // 四星保底了
            pool.lastGet4Count = 0; // 上一次获得四星计数清零
            pool.lastGet5Count++;
            pool.totalGet4Count++;
            if (!pool.lastGet4isUp) {  // 如果上一次4星不是UP角色
                tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, true));
                pool.lastGet4isUp = true; // 最后获得4星是
                pool.totalGet4UpCount++;
            } else {
                if (secureRandom.nextDouble() < r4UpProb) { // 选择是否为UP
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, true));
                    pool.lastGet4isUp = true;
                    pool.totalGet4UpCount++;
                } else {
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, false));
                    pool.lastGet4isUp = false;
                }
            }
        } else { // 三星物品（没其它情况了）
            pool.lastGet4Count++;
            pool.lastGet5Count++;
            tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 3));
        }
    }

}

