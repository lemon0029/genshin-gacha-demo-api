package me.yec.repository;

import me.yec.model.entity.genshin.item.GenshinWeapon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yec
 * @date 12/4/20 9:00 PM
 */
public interface GenshinWeaponRepository extends JpaRepository<GenshinWeapon, Long> {
}
