package me.yec.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 原神抽奖核心类
 * 脱了... 这个类作用不大...
 *
 * @author yec
 * @date 12/4/20 10:51 PM
 */
public class LotteryOM {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final double prob4; // 单抽 4 星概率
    private final double prob5; // 单抽 5 星概率

    private final double prob4up; // 第 8 或 9 次四星概率提升
    private final double prob5up; // 第 75 到 89 次之间五星概率提升

    // 保底
    private final int baodi4threshold; // 4 星保底
    private final int baodi5threshold; // 5 星保底（武器为80）

    // 软保底
    private final int _baodi4threshold; // 4 星概率提升抽取次数阀门
    private final int _baodi5threshold; // 5 星概率提升抽取次数阀门


    public LotteryOM(double prob4,
                     double prob5,
                     double prob4up,
                     double prob5up,
                     int baodi4threshold,
                     int baodi5threshold,
                     int _baodi4threshold,
                     int _baodi5threshold) {
        this.prob4 = prob4;
        this.prob5 = prob5;
        this.prob4up = prob4up;
        this.prob5up = prob5up;
        this._baodi4threshold = _baodi4threshold;
        this._baodi5threshold = _baodi5threshold;
        this.baodi4threshold = baodi4threshold;
        this.baodi5threshold = baodi5threshold;
    }

    /**
     * 实现简易的抽奖方法
     *
     * @param n             抽奖次数
     * @param lastGet4Count 上一次获得4星到现在抽了几次
     * @param lastGet5Count 上一次获得5星到现在抽了几次
     * @return 抽奖结果集合（包含 3 4 5）
     */
    public List<Integer> gacha(int n, int lastGet4Count, int lastGet5Count) {

        List<Integer> result = new ArrayList<>(); // 抽奖结果（存放 3 4 5 ...）

        for (int i = 0; i < n; i++) {
            boolean get4Star = false; // 默认未获得4星
            boolean get5Star = false; // 默认未获得5星
            double v = RANDOM.nextDouble(); // 随机

            if (lastGet5Count < this._baodi5threshold) {
                if (v < this.prob5) get5Star = true;
                else {
                    if (lastGet4Count < this._baodi4threshold) {
                        if (v < this.prob4 + this.prob5) get4Star = true;
                    } else if (lastGet4Count < this.baodi4threshold - 1) {
                        if (v < this.prob4up + this.prob5) get4Star = true;
                    } else {
                        get4Star = true;
                    }
                }
            } else if (lastGet5Count < this.baodi5threshold - 1) {
                if (v < this.prob5up) get5Star = true;
                else {
                    if (lastGet4Count < this._baodi4threshold) {
                        if (v < this.prob4 + this.prob5up) get4Star = true;
                    } else if (lastGet4Count < this.baodi4threshold - 1) {
                        if (v < this.prob4up + this.prob5up) get4Star = true;
                    } else {
                        get4Star = true;
                    }
                }
            } else {
                get5Star = true;
            }

            if (get5Star) {
                result.add(5);
                lastGet4Count++;
                lastGet5Count = 0;
            } else if (get4Star) {
                result.add(4);
                lastGet5Count++;
                lastGet4Count = 0;
            } else {
                result.add(3);
                lastGet4Count++;
                lastGet5Count++;
            }

        }

        return result;
    }

}
