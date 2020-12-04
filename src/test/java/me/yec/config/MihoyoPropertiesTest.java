package me.yec.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yec
 * @date 12/4/20 5:55 PM
 */
@SpringBootTest
public class MihoyoPropertiesTest {

    @Autowired
    private MihoyoProperties mihoyoProperties;

    @Test
    void simpleTest() {
        System.out.println(mihoyoProperties.getAccountId());
        System.out.println(mihoyoProperties.getCookieToken());
    }
}
