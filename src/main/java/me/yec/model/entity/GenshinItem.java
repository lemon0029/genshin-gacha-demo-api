package me.yec.model.entity;

/**
 * @author yec
 * @date 12/4/20 8:12 PM
 */
public class GenshinItem {

    protected ItemType type;

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public enum ItemType {
        WEAPON, CHARACTER
    }

}
