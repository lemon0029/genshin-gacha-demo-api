package me.yec.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yec
 * @date 12/4/20 5:55 PM
 */
@SpringBootTest
public class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    void simpleTest() {
        System.out.println(appProperties.getAccountId());
        System.out.println(appProperties.getCookieToken());
    }
}
