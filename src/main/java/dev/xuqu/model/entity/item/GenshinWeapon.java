package dev.xuqu.model.entity.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 原神武器实体类
 *
 * @author yec
 * @date 12/4/20 8:12 PM
 */
@Data
@Entity
@Table(name = "genshin_weapon")
@EqualsAndHashCode(callSuper = true)
public class GenshinWeapon extends GenshinItem {

    @Id
    private Long id;

    // 武器名
    private String name;

    // 武器星级
    private Integer ranting;

    // 武器图片
    private String avatar;

    public GenshinWeapon() {
        type = GenshinItemType.CHARACTER;
    }
}
