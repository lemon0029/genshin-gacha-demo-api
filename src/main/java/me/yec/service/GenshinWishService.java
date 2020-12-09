package me.yec.service;

import me.yec.core.LotteryUser;
import me.yec.model.dto.GenshinWishDTO;
import me.yec.model.dto.GenshinWishStatisticDTO;

/**
 * @author yec
 * @date 12/6/20 6:43 PM
 */
public interface GenshinWishService {

    /**
     * 获取奖池统计数据
     *
     * @param poolId      奖池ID
     * @param lotteryUser 抽奖用户
     * @return GenshinWishStatisticDTO 对象
     */
    GenshinWishStatisticDTO findStatisticByPoolId(String poolId, LotteryUser lotteryUser);

    /**
     * 重置奖池信息
     *
     * @param poolId      奖池ID
     * @param lotteryUser 抽奖用户
     */
    void reset(String poolId, LotteryUser lotteryUser);

    /**
     * 抽奖方法
     *
     * @param n           抽奖次数
     * @param poolId      奖池ID
     * @param lotteryUser 抽奖对象
     * @return 抽奖DTO
     */
    GenshinWishDTO wishByPoolId(int n, String poolId, LotteryUser lotteryUser);
}
