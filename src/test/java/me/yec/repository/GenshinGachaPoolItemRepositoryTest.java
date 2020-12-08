package me.yec.repository;


import me.yec.model.entity.gacha.GenshinGachaPoolItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author yec
 * @date 12/8/20 1:05 PM
 */
@SpringBootTest
class GenshinGachaPoolItemRepositoryTest {

    @Autowired
    private GenshinGachaPoolItemRepository genshinGachaPoolItemRepository;

    @Test
    void findAllByGachaIdTest() {
        double prob4 = 0;
        double prob5 = 0;
        double prob3 = 0;

        List<GenshinGachaPoolItem> allByGachaId = genshinGachaPoolItemRepository.findAllByGachaId(213);
        for (GenshinGachaPoolItem genshinGachaPoolItem : allByGachaId) {
            if (genshinGachaPoolItem.getRanting() == 5) prob5 += genshinGachaPoolItem.getProb();
            if (genshinGachaPoolItem.getRanting() == 4) prob4 += genshinGachaPoolItem.getProb();
            if (genshinGachaPoolItem.getRanting() == 3) prob3 += genshinGachaPoolItem.getProb();
        }

        System.out.println(prob3);
        System.out.println(prob4);
        System.out.println(prob5);
    }


}