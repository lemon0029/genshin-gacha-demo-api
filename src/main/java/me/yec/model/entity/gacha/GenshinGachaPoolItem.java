package me.yec.model.entity.gacha;

import lombok.Data;

import javax.persistence.*;

/**
 * @author yec
 * @date 12/6/20 5:11 PM
 */
@Data
@Entity
@Table(name = "genshin_gacha_pool_item")
public class GenshinGachaPoolItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 自增长ID

    @Column(name = "item_id")
    private Long itemId; // 对应角色或者武器ID
    private String type; // 类型
    private String name; // 名称
    private Integer ranting; // 星级
    @Column(name = "gacha_id")
    private String gachaId; // 抽奖池ID
    private Boolean up; // 是否UP
}
