package me.yec.service;

import me.yec.model.entity.item.GenshinItem;

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
}
