package me.yec.service;

import me.yec.model.entity.item.GenshinCharacter;
import me.yec.model.entity.item.GenshinItem;
import me.yec.model.entity.item.GenshinWeapon;

import java.util.List;

/**
 * @author yec
 * @date 12/4/20 9:38 PM
 */
public interface GenshinItemService {
    /**
     * 获取所有的原神武器/角色
     *
     * @param type  武器/角色
     * @param sort  排序属性
     * @param order 排序方向
     * @return GenshinItem 集合
     */
    List<? extends GenshinItem> findAllGenshinItem(String type, String sort, String order);

    List<GenshinCharacter> findAllGenshinCharacter(String sort, String order);

    List<GenshinWeapon> findAllGenshinWeapon(String sort, String order);

    /**
     * 根据 ID 从数据库中提取数据
     *
     * @param itemIds 指定ID列表
     * @return GenshinItem 对象列表
     */
    List<GenshinItem> findGiftById(List<Long> itemIds);
}
