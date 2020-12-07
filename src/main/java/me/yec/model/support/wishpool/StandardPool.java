package me.yec.model.support.wishpool;

import java.io.Serializable;

/**
 * 原神常驻池
 *
 * @author yec
 * @date 12/4/20 10:40 PM
 */
public class StandardPool extends GenshinWishPool implements Serializable {

    private static final long serialVersionUID = -1982051016224188340L;

    public StandardPool() {
        type = GenshinWishPoolType.STANDARD;
    }
}
