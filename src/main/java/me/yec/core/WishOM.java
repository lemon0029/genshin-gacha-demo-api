package me.yec.core;

import lombok.extern.slf4j.Slf4j;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.repository.GenshinGachaPoolItemRepository;
import me.yec.util.GenerateWishProb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 调度抽奖
 *
 * @author yec
 * @date 12/6/20 7:04 PM
 */
@Slf4j
@Component
@Scope("prototype")
public class WishOM {

    private final GenshinGachaPoolInfoRepository gachaPoolInfoRepository;
    private final GenshinGachaPoolItemRepository gachaPoolItemRepository;

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
        Map<String, Number> probBy = GenerateWishProb.getProbBy(gachaPoolInfo); // 获取池子相关概率

        if (gachaType == 200) { // 200 是常驻池
            return StandardWishOM.wish(probBy, lotteryUser, allItem, n);
        } else { // 角色活动池和武器活动池差不多
            return EventWishOM.wish(probBy, lotteryUser, poolId, allItem, n);
        }
    }

}
