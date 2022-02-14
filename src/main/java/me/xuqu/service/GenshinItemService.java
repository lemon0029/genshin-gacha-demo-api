package me.xuqu.service;

import me.xuqu.model.dto.GenshinInventoryDTO;
import me.xuqu.model.entity.item.GenshinCharacter;
import me.xuqu.model.entity.item.GenshinItem;
import me.xuqu.model.entity.item.GenshinWeapon;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yec
 * @date 12/4/20 9:38 PM
 */
public interface GenshinItemService {
    /**
     * 获取所有的原神武器/角色
     *
     * @return GenshinItem 集合
     */
    List<? extends GenshinItem> findAllGenshinItem();

    /**
     * 获取所有的角色信息
     *
     * @return GenshinCharacter 集合
     */
    List<GenshinCharacter> findAllGenshinCharacter(Pageable pageable);

    List<GenshinWeapon> findAllGenshinWeapon(Pageable pageable);

    /**
     * 根据指定的ID获取 GenshinInventoryDTO 对象
     * 对物品类型分类未做处理，也就是当前只实现了返回所有物品
     *
     * @param ids   指定物品ID集合
     * @param type  物品类型
     * @param sort  排序属性
     * @param order 排序方向
     * @return GenshinInventoryDTO 集合
     */
    List<GenshinInventoryDTO> findAllByIds(List<Long> ids, String type, String sort, String order);

    /**
     * 根据 ID 从数据库中提取数据
     *
     * @param itemIds 指定ID列表
     * @return GenshinItem 对象列表
     */
    List<GenshinItem> findGiftById(List<Long> itemIds);
}
