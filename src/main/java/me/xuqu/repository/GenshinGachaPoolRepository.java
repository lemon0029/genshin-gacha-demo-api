package me.xuqu.repository;

import me.xuqu.model.entity.gacha.GenshinGachaPool;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yec
 * @date 12/6/20 5:01 PM
 */
public interface GenshinGachaPoolRepository extends JpaRepository<GenshinGachaPool, String> {
}
