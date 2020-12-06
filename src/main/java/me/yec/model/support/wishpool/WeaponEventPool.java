package me.yec.model.support.wishpool;

import java.io.Serializable;

/**
 * @author yec
 * @date 12/4/20 10:41 PM
 */
public class WeaponEventPool extends GenshinEventWishPool implements Serializable {
    public WeaponEventPool() {
        type = GenshinWishPoolType.WEAPON_EVENT;
    }
}
