package me.xuqu.service.impl;

import lombok.RequiredArgsConstructor;
import me.xuqu.model.dto.GenshinInventoryDTO;
import me.xuqu.model.entity.item.GenshinCharacter;
import me.xuqu.model.entity.item.GenshinItem;
import me.xuqu.model.entity.item.GenshinWeapon;
import me.xuqu.repository.GenshinCharacterRepository;
import me.xuqu.repository.GenshinWeaponRepository;
import me.xuqu.service.GenshinItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * 抽奖物品业务实现类
 *
 * @author yec
 * @date 12/4/20 9:39 PM
 */
@Service
@RequiredArgsConstructor
public class GenshinItemServiceImpl implements GenshinItemService {

    private final GenshinCharacterRepository genshinCharacterRepository;
    private final GenshinWeaponRepository genshinWeaponRepository;

    @Override
    public List<? extends GenshinItem> findAllGenshinItem() {
        List<GenshinCharacter> characters = genshinCharacterRepository.findAll();
        List<GenshinWeapon> weapons = genshinWeaponRepository.findAll();
        List<GenshinItem> genshinItems = new ArrayList<>();
        genshinItems.addAll(characters);
        genshinItems.addAll(weapons);
        return genshinItems;
    }

    public List<GenshinItem> findGiftById(List<Long> itemIds) {
        List<GenshinCharacter> characters = genshinCharacterRepository.findAllById(itemIds);
        List<GenshinWeapon> weapons = genshinWeaponRepository.findAllById(itemIds);
        List<GenshinItem> genshinItems = new ArrayList<>();
        for (Long itemId : itemIds) {
            for (GenshinCharacter character : characters) {
                if (character.getId().equals(itemId)) {
                    genshinItems.add(character);
                    break;
                }
            }

            for (GenshinWeapon weapon : weapons) {
                if (weapon.getId().equals(itemId)) {
                    genshinItems.add(weapon);
                    break;
                }
            }
        }
        return genshinItems;
    }

    /**
     * 给定字符串指定排序方向
     *
     * @param order 字符串（asc...）
     * @return Direction 对象
     */
    private Sort.Direction checkOrder(String order) {
        if ("asc".equalsIgnoreCase(order)) {
            return Sort.Direction.ASC;
        } else if ("desc".equalsIgnoreCase(order)) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }


    @Override
    public List<GenshinCharacter> findAllGenshinCharacter(Pageable pageable) {
        return genshinCharacterRepository.findAll(pageable).getContent();
    }

    @Override
    public List<GenshinWeapon> findAllGenshinWeapon(Pageable pageable) {
        return genshinWeaponRepository.findAll(pageable).getContent();
    }

    @Override
    public List<GenshinInventoryDTO> findAllByIds(List<Long> ids, String type, String sort, String order) {
        List<GenshinInventoryDTO> genshinInventoryDTOS = new ArrayList<>();
        List<GenshinWeapon> weaponList = genshinWeaponRepository.findAllById(ids);
        List<GenshinCharacter> characterList = genshinCharacterRepository.findAllById(ids);

        HashSet<Long> longs = new HashSet<>(ids);
        for (Long id : longs) {

            GenshinInventoryDTO genshinInventoryDTO = new GenshinInventoryDTO();
            Optional<GenshinWeapon> weaponOptional = weaponList
                    .stream()
                    .filter(genshinWeapon -> genshinWeapon.getId().equals(id))
                    .findFirst();
            // 如果是武器
            if (weaponOptional.isPresent()) {
                GenshinWeapon o = weaponOptional.get();
                BeanUtils.copyProperties(o, genshinInventoryDTO);
                long count = ids.stream().filter(x -> x.equals(id)).count();
                genshinInventoryDTO.setCount(count);
            } else {
                Optional<GenshinCharacter> characterOptional = characterList
                        .stream()
                        .filter(genshinCharacter -> genshinCharacter.getId().equals(id))
                        .findFirst();

                // 不是武器就一定是角色
                assert characterOptional.isPresent();
                GenshinCharacter genshinCharacter = characterOptional.get();
                BeanUtils.copyProperties(genshinCharacter, genshinInventoryDTO);
                long count = ids.stream().filter(x -> x.equals(id)).count();
                genshinInventoryDTO.setCount(count);
            }

            genshinInventoryDTOS.add(genshinInventoryDTO);
        }

        // 按星级降序
        genshinInventoryDTOS.sort((t0, t1) -> t1.getRanting() - t0.getRanting());

        return genshinInventoryDTOS;
    }
}
