package me.yec.repository;

import me.yec.model.entity.item.GenshinCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yec
 * @date 12/4/20 8:18 PM
 */
public interface GenshinCharacterRepository extends JpaRepository<GenshinCharacter, Long> {
}
