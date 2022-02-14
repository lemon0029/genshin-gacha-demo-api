package me.xuqu.model.support.wishpool;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yec
 * @date 12/4/20 10:41 PM
 */
public class WeaponEventPool extends GenshinEventWishPool implements Serializable {

    @Serial
    private static final long serialVersionUID = 3460941671676049343L;

    public WeaponEventPool() {
        type = GenshinWishPoolType.WEAPON_EVENT;
    }
}
