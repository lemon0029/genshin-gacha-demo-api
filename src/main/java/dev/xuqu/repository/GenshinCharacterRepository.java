package dev.xuqu.repository;

import dev.xuqu.model.entity.item.GenshinCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author yec
 * @date 12/4/20 8:18 PM
 */
public interface GenshinCharacterRepository extends JpaRepository<GenshinCharacter, Long> {
    Optional<GenshinCharacter> findByName(String name);
}
