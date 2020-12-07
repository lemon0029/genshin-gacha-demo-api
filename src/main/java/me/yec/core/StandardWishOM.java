package me.yec.core;

import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.model.support.wishpool.StandardPool;
import me.yec.util.WishOMUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 常驻池抽奖
 *
 * @author yec
 * @date 12/7/20 3:11 PM
 */
@Component
public class StandardWishOM {

    /**
     * 常驻池祈愿
     *
     * @param items 池中的奖励
     * @param n     抽奖次数
     */
    public List<Long> wish(LotteryUser lotteryUser, List<GenshinGachaPoolItem> items, int n) {
        StandardPool pool = lotteryUser.getStandardPool();
        int count4 = pool.lastGet4Count; // 获取抽奖角色上一次常驻池出4星的到现在次数
        int count5 = pool.lastGet5Count; // 获取抽奖角色上一次常驻池出5星的到现在次数
        List<Integer> gacha = LotteryOM.gacha(n, count4, count5);
        List<Long> tempItemIds = new ArrayList<>();
        for (Integer a : gacha) {
            if (a == 5) {
                if (pool.lastGet5Count == 89) pool.get5ByBaodiCount++;
                pool.lastGet4Count++;
                pool.lastGet5Count = 0;
                pool.totalGet5Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 5));
            } else if (a == 4) {
                if (pool.lastGet4Count == 9) pool.get4ByBaodiCount++;
                pool.lastGet4Count = 0;
                pool.lastGet5Count++;
                pool.totalGet4Count++;
                tempItemIds.add(WishOMUtils.randomGiftByRanting(items, 4));
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

}
