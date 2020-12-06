package me.yec.core;

import lombok.extern.slf4j.Slf4j;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.model.support.wishpool.StandardPool;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.repository.GenshinGachaPoolItemRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yec
 * @date 12/6/20 7:04 PM
 */
@Slf4j
@Component
@Scope("prototype")
public class WishOM {

    private final GenshinGachaPoolInfoRepository gachaPoolInfoRepository;
    private final GenshinGachaPoolItemRepository gachaPoolItemRepository;
    private final SecureRandom random = new SecureRandom();

    private LotteryUser lotteryUser;

    public WishOM(GenshinGachaPoolInfoRepository gachaPoolInfoRepository,
                  GenshinGachaPoolItemRepository gachaPoolItemRepository) {
        this.gachaPoolInfoRepository = gachaPoolInfoRepository;
        this.gachaPoolItemRepository = gachaPoolItemRepository;
    }

    public void setLotteryUser(LotteryUser lotteryUser) {
        this.lotteryUser = lotteryUser;
    }

    private GenshinGachaPoolInfo findGachaPoolInfo(String poolId) {
        Optional<GenshinGachaPoolInfo> gachaPoolInfoOptional = gachaPoolInfoRepository.findById(poolId);
        // 如果没有直接抛出异常，不做任何处理
        return gachaPoolInfoOptional.orElseThrow(
                () -> {
                    log.error("gacha pool id[{}] not found", poolId);
                    return new RuntimeException(String.format("gacha pool id[%s] not found", poolId));
                });
    }

    public List<Long> wishByPoolId(String poolId, int n) {
        GenshinGachaPoolInfo gachaPoolInfo = findGachaPoolInfo(poolId);// 获取池子的类型
        Integer gachaId = gachaPoolInfo.getGachaId();
        Integer gachaType = gachaPoolInfo.getGachaType();
        List<GenshinGachaPoolItem> allItem = gachaPoolItemRepository.findAllByGachaId(gachaId); // 获取池子中的奖励
        if (gachaType == 200) { // 200 是常驻池
            return standardPoolWish(allItem, n);
        } else {
            return null;
        }
    }


    /**
     * 常驻池祈愿
     *
     * @param items 池中的奖励
     * @param n     抽奖次数
     */
    private List<Long> standardPoolWish(List<GenshinGachaPoolItem> items, int n) {
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
                tempItemIds.add(randomGiftByRanting(items, 5));
            } else if (a == 4) {
                if (pool.lastGet4Count == 9) pool.get4ByBaodiCount++;
                pool.lastGet4Count = 0;
                pool.lastGet5Count++;
                pool.totalGet4Count++;
                tempItemIds.add(randomGiftByRanting(items, 4));
            } else if (a == 3) {
                pool.lastGet4Count++;
                pool.lastGet5Count++;
                tempItemIds.add(randomGiftByRanting(items, 3));
            }
        }
        pool.wishInventory.addAll(tempItemIds);
        pool.totalWishCount += n;
        return tempItemIds;
    }


    /**
     * 根据给定的奖励和等级随机抽取一个
     *
     * @param items   给定的奖励
     * @param ranting 等级
     * @return 奖励品ID
     */
    private Long randomGiftByRanting(List<GenshinGachaPoolItem> items, Integer ranting) {
        // 提取所有等级为给定 ranting 的物品
        List<GenshinGachaPoolItem> gachaPoolItems = items.stream()
                .filter(item -> item.getRanting().equals(ranting))
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
    private Long randomGachaPoolItem(List<GenshinGachaPoolItem> items) {
        // 这样写可能优点问题...
        int i = random.nextInt(items.size());
        GenshinGachaPoolItem gachaPoolItem = items.get(i);
        return gachaPoolItem.getItemId();
    }
}
