package me.yec.repository;

import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yec
 * @date 12/6/20 5:12 PM
 */
public interface GenshinGachaPoolInfoRepository extends JpaRepository<GenshinGachaPoolInfo, String> {
}
