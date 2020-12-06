package me.yec.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author yec
 * @date 12/6/20 1:16 PM
 */
@SpringBootTest
class FetchGenshinGachaPoolDataTest {

    @Autowired
    private FetchGenshinGachaPoolData fetchGenshinGachaPoolData;

    @Test
    void fetchGachaList() {
        fetchGenshinGachaPoolData.fetchGachaList();
    }

    @Test
    void fetchGachaPoolInfo() {
        fetchGenshinGachaPoolData.fetchGachaPoolInfo();
    }
}