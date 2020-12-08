package me.yec.core;

import me.yec.model.support.wishpool.StandardPool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yec
 * @date 12/6/20 12:23 PM
 */
@SpringBootTest
class LotteryOMTest {

    @Autowired
    private WishOM wishOM;


    @Test
    void gacha() {
        LotteryUser lotteryUser = new LotteryUser();
        wishOM.setLotteryUser(lotteryUser);
        wishOM.wishByPoolId("4e92901acfa995f3eed9a7a26ab97acc6f65c6", 100);
        StandardPool standardPool = lotteryUser.getStandardPool();
        System.out.println(standardPool);
    }

}