package me.xuqu.core;

import me.xuqu.exception.AppException;
import me.xuqu.model.entity.gacha.GenshinGachaPoolInfo;
import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;
import me.xuqu.repository.GenshinGachaPoolInfoRepository;
import me.xuqu.repository.GenshinGachaPoolItemRepository;
import me.xuqu.util.GenerateWishProb;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 通过给定的池子 ID 和 抽奖次数抽奖获取物品的 ID
     *
     * @param poolId 池子唯一ID
     * @param n      抽奖次数
     * @return 抽奖获得的物品 ID 集合
     */
    public List<Long> wishByPoolId(String poolId, int n) {

        GenshinGachaPoolInfo gachaPoolInfo = findGachaPoolInfo(poolId);// 获取池子的类型
        String gachaId = gachaPoolInfo.getId();
        Integer gachaType = gachaPoolInfo.getGachaType();

        List<GenshinGachaPoolItem> allItem = gachaPoolItemRepository.findAllByGachaId(gachaId); // 获取池子中的奖励
        Map<String, Number> probBy = GenerateWishProb.getProbBy(gachaPoolInfo); // 获取池子相关概率

        if (gachaType == 200) { // 200 是常驻池
            return StandardWishOM.wish(probBy, lotteryUser, poolId, allItem, n);
        } else { // 角色活动池和武器活动池差不多
            return EventWishOM.wish(probBy, lotteryUser, poolId, allItem, n);
        }
    }

    /**
     * 通过奖池 ID 获取奖池的具体信息
     *
     * @param poolId 奖池 ID
     * @return 奖池的具体信息
     */
    private GenshinGachaPoolInfo findGachaPoolInfo(String poolId) {
        Optional<GenshinGachaPoolInfo> gachaPoolInfoOptional = gachaPoolInfoRepository.findById(poolId);
        // 如果没有直接抛出异常，不做任何处理
        return gachaPoolInfoOptional.orElseThrow(
                () -> {
                    log.error("gacha pool id[{}] not found", poolId);
                    return new AppException(String.format("gacha pool id[%s] not found", poolId));
                });
    }
}
