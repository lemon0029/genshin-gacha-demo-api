package me.yec.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author yec
 * @date 12/4/20 8:36 PM
 */
@SpringBootTest
class FetchGenshinItemDataTest {

    @Autowired
    private FetchGenshinItemData fetchGenshinItemData;

    @Test
    void fetchCharacters() {
        fetchGenshinItemData.fetchCharacters();
    }

    @Test
    void fetWeapons() {
        fetchGenshinItemData.fetWeapons();
    }
}