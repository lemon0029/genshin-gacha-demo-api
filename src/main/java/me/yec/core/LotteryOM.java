package me.yec.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 原神抽奖核心算法
 *
 * @author yec
 * @date 12/4/20 10:51 PM
 */
public class LotteryOM {
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final double RATE4 = 0.051; // 单抽 4 星概率
    private static final double RATE5 = 0.006; // 单抽 5 星概率

    private static final double RATE4UP = 0.511; // 第 8 或 9 次四星概率提升
    private static final double RATE5UP = 0.324; // 第 75 到 89 次之间五星概率提升

    // 软保底
    private static final int THRESHOLD4 = 8; // 4 星概率提升抽取次数阀门
    private static final int THRESHOLD5 = 75; // 5 星概率提升抽取次数阀门

    /**
     * 实现简易的抽奖方法
     *
     * @param n             抽奖次数
     * @param lastGet4Count 上一次获得4星到现在抽了几次
     * @param lastGet5Count 上一次获得5星到现在抽了几次
     * @return 抽奖结果集合（包含 3 4 5）
     */
    public static List<Integer> gacha(int n, int lastGet4Count, int lastGet5Count) {

        List<Integer> result = new ArrayList<>(); // 抽奖结果（存放 3 4 5 ...）

        for (int i = 0; i < n; i++) {
            boolean get4Star = false; // 默认未获得4星
            boolean get5Star = false; // 默认未获得5星
            double v = RANDOM.nextDouble(); // 随机

            if (lastGet5Count < THRESHOLD5) {
                if (v < RATE5) get5Star = true;
                else {
                    if (lastGet4Count < THRESHOLD4) {
                        if (v < RATE4 + RATE5) get4Star = true;
                    } else if (lastGet4Count < 10) {
                        if (v < RATE4UP + RATE5) get4Star = true;
                    } else {
                        get4Star = true;
                    }
                }
            } else if (lastGet5Count < 90) {
                if (v < RATE5UP) get5Star = true;
                else {
                    if (lastGet4Count < THRESHOLD4) {
                        if (v < RATE4 + RATE5UP) get4Star = true;
                    } else if (lastGet4Count < 10) {
                        if (v < RATE4UP + RATE5UP) get4Star = true;
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
