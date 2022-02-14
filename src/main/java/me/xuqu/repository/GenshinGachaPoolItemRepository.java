package me.xuqu.repository;

import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 5:12 PM
 */
public interface GenshinGachaPoolItemRepository extends JpaRepository<GenshinGachaPoolItem, Integer>,
        JpaSpecificationExecutor<GenshinGachaPoolItem> {

    List<GenshinGachaPoolItem> findAllByGachaId(String gachaId);
}
