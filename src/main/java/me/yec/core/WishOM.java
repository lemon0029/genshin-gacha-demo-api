package me.yec.core;

import lombok.extern.slf4j.Slf4j;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.repository.GenshinGachaPoolItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author yec
 * @date 12/6/20 7:04 PM
 */
@Slf4j
@Component
public class WishOM {

    private final GenshinGachaPoolInfoRepository gachaPoolInfoRepository;
    private final GenshinGachaPoolItemRepository gachaPoolItemRepository;
    private final StandardWishOM standardWishOM;
    private final EventWishOM eventWishOM;

    private LotteryUser lotteryUser;

    public WishOM(GenshinGachaPoolInfoRepository gachaPoolInfoRepository,
                  GenshinGachaPoolItemRepository gachaPoolItemRepository,
                  StandardWishOM standardWishOM,
                  EventWishOM eventWishOM) {
        this.gachaPoolInfoRepository = gachaPoolInfoRepository;
        this.gachaPoolItemRepository = gachaPoolItemRepository;
        this.standardWishOM = standardWishOM;
        this.eventWishOM = eventWishOM;
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
            return standardWishOM.wish(lotteryUser, allItem, n);
        } else { // 角色活动池和武器活动池差不多
            return eventWishOM.wish(lotteryUser, poolId, allItem, n);
        }
    }

}
