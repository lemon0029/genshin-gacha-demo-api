package dev.xuqu.service;

import dev.xuqu.core.LotteryUser;

/**
 * @author yec
 * @date 12/6/20 6:52 PM
 */
public interface LotteryUserService {
    /**
     * 初始化一个抽奖用户
     *
     * @return LotteryUser对象
     */
    LotteryUser init();
}
