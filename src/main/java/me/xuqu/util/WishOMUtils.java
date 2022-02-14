package me.xuqu.util;

import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽奖工具类
 *
 * @author yec
 * @date 12/7/20 3:17 PM
 */
public class WishOMUtils {

    private static final SecureRandom random = new SecureRandom();

    /**
     * 根据给定的奖励和等级随机抽取一个
     *
     * @param items   给定的奖励
     * @param ranting 等级
     * @return 奖励品ID
     */
    public static Long randomGiftByRanting(List<GenshinGachaPoolItem> items, Integer ranting) {
        // 提取所有等级为给定 ranting 的物品
        List<GenshinGachaPoolItem> gachaPoolItems = items.stream()
                .filter(item -> item.getRanting().equals(ranting))
                .collect(Collectors.toList());

        // 获取奖励的ID
        return randomGachaPoolItem(gachaPoolItems);
    }

    public static Long randomGiftByUpAndRanting(List<GenshinGachaPoolItem> items, Integer ranting, boolean isUp) {
        // 提取所有等级为给定 ranting 的物品
        List<GenshinGachaPoolItem> gachaPoolItems = items.stream()
                .filter(item -> item.getRanting().equals(ranting) && item.getUp().equals(isUp))
                .collect(Collectors.toList());
        // 获取奖励的ID
        return randomGachaPoolItem(gachaPoolItems);
    }

    /**
     * 从给定的奖励中随机抽取一个
     *
     * @param items 指定奖励列表
     * @return 奖励ID
     */
    public static Long randomGachaPoolItem(List<GenshinGachaPoolItem> items) {
        // 这样写似乎有点问题...
        int i = random.nextInt(items.size());
        GenshinGachaPoolItem gachaPoolItem = items.get(i);
        return gachaPoolItem.getItemId();
    }
}
