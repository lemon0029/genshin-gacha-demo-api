package me.yec.service;

import me.yec.core.LotteryUser;

/**
 * @author yec
 * @date 12/6/20 8:06 PM
 */
public interface SimpleAuthService {
    /**
     * 简单的授权认证
     *
     * @return vid （也就是 session id）
     */
    String doAuth();

    /**
     * 根据 session id 从 redis 中取出 LotteryUser 对象
     *
     * @param vid aka session id
     * @return LotteryUser 对象
     */
    LotteryUser getCurrentUser(String vid);

    /**
     * 更新抽奖用户对象
     *
     * @param vid         aka session id
     * @param lotteryUser 抽奖对象
     */
    void updateUser(String vid, LotteryUser lotteryUser);
}
