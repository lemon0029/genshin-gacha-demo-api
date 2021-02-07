package dev.xuqu.model.entity.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 原神角色实体类
 *
 * @author yec
 * @date 12/4/20 8:12 PM
 */
@Data
@Entity
@Table(name = "genshin_character")
@EqualsAndHashCode(callSuper = true)
public class GenshinCharacter extends GenshinItem {

    @Id
    private Long id;

    // 角色名称
    private String name;

    // 角色头像
    private String avatar;

    // 角色属性
    @Column(name = "character_attribute_id")
    private Integer CharacterAttrId;

    // 角色星级（5星，4星...）
    @Column(name = "ranting")
    private Integer ranting;

    public GenshinCharacter() {
        type = GenshinItemType.CHARACTER;
    }
}
