package me.yec.core;

import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.model.support.wishpool.GenshinEventWishPool;
import me.yec.util.WishOMUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动池抽奖（武器池和角色池用同样的逻辑...）
 *
 * @author yec
 * @date 12/7/20 3:14 PM
 */
@Component
public class EventWishOM {

    private final SecureRandom random = new SecureRandom();

    /**
     * 角色活动池抽奖
     *
     * @param poolId 抽奖池ID
     * @param items  抽奖物品
     * @param n      抽奖次数
     * @return 奖品ID
     */
    public List<Long> wish(LotteryUser lotteryUser, String poolId, List<GenshinGachaPoolItem> items, int n) {
        GenshinEventWishPool pool = lotteryUser.getEventPool(poolId);
        int count4 = pool.lastGet4Count; // 获取抽奖角色上一次常驻池出4星的到现在次数
        int count5 = pool.lastGet5Count; // 获取抽奖角色上一次常驻池出5星的到现在次数
        List<Integer> gacha = LotteryOM.gacha(n, count4, count5); // 获取抽奖结果
        List<Long> tempItemIds = new ArrayList<>();
        for (Integer a : gacha) {
            if (a == 5) {
                if (pool.lastGet5Count == 89) pool.get5ByBaodiCount++;
                pool.lastGet4Count++;
                pool.lastGet5Count = 0;
                pool.totalGet5Count++;
                if (!pool.lastGet5isUp) {  // 如果上一次5星不是UP角色
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, true));
                    pool.lastGet5isUp = true; // 最后获得五星是
                    pool.totalGet5UpCount++;
                } else {
                    if (timeToUp(items, 5)) { // 选择是否为UP
                        tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, true));
                        pool.totalGet5UpCount++;
                        pool.lastGet5isUp = true;
                    } else {
                        tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 5, false));
                        pool.lastGet5isUp = false;
                    }
                }
            } else if (a == 4) {
                if (pool.lastGet4Count == 9) pool.get4ByBaodiCount++;
                pool.lastGet4Count = 0;
                pool.lastGet5Count++;
                pool.totalGet4Count++;
                if (!pool.lastGet4isUp) {  // 如果上一次4星不是UP角色
                    tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, true));
                    pool.lastGet4isUp = true; // 最后获得4星是
                    pool.totalGet4UpCount++;
                } else {
                    if (timeToUp(items, 4)) { // 选择是否为UP
                        tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, true));
                        pool.lastGet4isUp = true;
                        pool.totalGet4UpCount++;
                    } else {
                        tempItemIds.add(WishOMUtils.randomGiftByUpAndRanting(items, 4, false));
                        pool.lastGet4isUp = false;
                    }
                }
            } else if (a == 3) {
                pool.lastGet4Count++;
                pool.lastGet5Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 3));
            }
        }
        pool.wishInventory.addAll(tempItemIds);
        pool.totalWishCount += n;
        return tempItemIds;
    }

    /**
     * 判断此次是否为UP物品
     *
     * @param items   奖池列表
     * @param ranting 物品等级
     * @return 为 true 表示这次为UP物品
     */
    private boolean timeToUp(List<GenshinGachaPoolItem> items, int ranting) {
        double upProb = 0;
        double nonUpProb = 0;
        for (GenshinGachaPoolItem item : items) {
            if (item.getRanting() == ranting) {
                if (item.getUp()) {
                    upProb += item.getProb();
                } else {
                    nonUpProb += item.getProb();
                }
            }
        }
        upProb = upProb / (upProb + nonUpProb);
        return random.nextDouble() < upProb;
    }
}
