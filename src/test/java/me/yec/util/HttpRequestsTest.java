package me.yec.util;

import me.yec.config.AppProperties;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yec
 * @date 12/4/20 6:19 PM
 */
@SpringBootTest
public class HttpRequestsTest {

    @Autowired
    private AppProperties appProperties;
    private BasicHeader cookie;

    @BeforeEach
    void initCookie() {
        String accountId = appProperties.getAccountId();
        String cookieToken = appProperties.getCookieToken();
        cookie = new BasicHeader("cookie", String.format("account_id=%s;cookie_token=%s", accountId, cookieToken));
    }

    @Test
    void simpleTest() {
        String url = "https://api-takumi.mihoyo.com/event/e20200928calculate/v1/item/filter";
        String s = Requests.get(url, cookie);
        System.out.println(s);
    }

    @Test
    void postTest() {
        String url = "https://api-takumi.mihoyo.com/event/e20200928calculate/v1/avatar/list";
        String data = "{\"page\": 1, \"size\": 100}";
        System.out.println(Requests.post(url, cookie, data));
    }

    @Test
    void fetchPoolListTest() {
        String url = "https://webstatic.mihoyo.com/hk4e/gacha_info/cn_gf01/gacha/list.json";
        System.out.println(Requests.get(url));
    }
}
