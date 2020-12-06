package me.yec.model.support.wishpool;

import java.io.Serializable;

/**
 * @author yec
 * @date 12/4/20 10:41 PM
 */
public class CharacterEventPool extends GenshinEventWishPool implements Serializable {

    public CharacterEventPool() {
        type = GenshinWishPoolType.CHARACTER_EVENT;
    }

}
