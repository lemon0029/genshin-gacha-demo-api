package me.yec.core;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 12:23 PM
 */
class LotteryOMTest {

    @Test
    void gacha() {

        for (int i = 0; i < 10; i++) {
            LotteryUser lotteryUser = new LotteryUser();
            List<Integer> gacha = LotteryOM.gacha(10, 0, 0);
            System.out.println(gacha);
        }

    }

    @Test
    void gacha90() {
        LotteryUser lotteryUser = new LotteryUser();

        List<Integer> gacha = LotteryOM.gacha(90, 0, 0);
        for (Integer integer : gacha) {
            System.out.println(integer);
        }
    }
}