package me.xuqu.repository;

import me.xuqu.model.entity.item.GenshinWeapon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author yec
 * @date 12/4/20 9:00 PM
 */
public interface GenshinWeaponRepository extends JpaRepository<GenshinWeapon, Long> {
    Optional<GenshinWeapon> findByName(String name);
}
