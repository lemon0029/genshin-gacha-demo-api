package me.yec.core;

/**
 * 抽奖用户
 *
 * @author yec
 * @date 12/6/20 12:25 PM
 */
public class LotteryUser {
    public int lastGet5Count;
    public int lastGet4Count;

    public LotteryUser() {
        this.lastGet4Count = 0;
        this.lastGet5Count = 0;
    }
}
