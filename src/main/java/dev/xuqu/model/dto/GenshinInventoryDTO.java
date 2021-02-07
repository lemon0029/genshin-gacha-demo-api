package dev.xuqu.model.dto;

import lombok.Data;

/**
 * @author yec
 * @date 12/7/20 9:38 PM
 */
@Data
public class GenshinInventoryDTO {
    private Long id;
    // 角色名称
    private String name;
    // 角色头像
    private String avatar;
    // 角色属性
    private Integer CharacterAttrId;
    // 角色星级（5星，4星...）
    private Integer ranting;

    private long count;
}
