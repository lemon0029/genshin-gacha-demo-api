package me.yec.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
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
    @Column(name = "rank_v")
    private Integer rankV;

    // 武器图片
    private String avatar;

    public GenshinWeapon() {
        type = GenshinItemType.CHARACTER;
    }
}
