package me.xuqu.core;

import me.xuqu.model.support.wishpool.GenshinEventWishPool;
import me.xuqu.model.support.wishpool.GenshinWishPool;
import me.xuqu.model.support.wishpool.GenshinWishPoolType;
import me.xuqu.model.support.wishpool.StandardPool;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖用户类
 * - 用户的所有抽奖池信息
 *
 * @author yec
 * @date 12/6/20 12:25 PM
 */
public class LotteryUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 4965803945133604562L;

    public List<GenshinWishPool> wishPools = new ArrayList<>();

    public GenshinWishPool getPoolById(String poolId) {
        GenshinWishPool eventWishPool = null;
        for (GenshinWishPool wishPool : wishPools) {
            if (wishPool.wishPoolId.equals(poolId)) {
                eventWishPool = wishPool;
                break;
            }
        }
        assert eventWishPool != null;
        return eventWishPool;
    }

    /**
     * 获取常驻池，目前来说常驻池只有一个
     *
     * @return 常驻池对象
     */
    public StandardPool getStandardPool() {
        StandardPool standardPool = null;
        for (GenshinWishPool wishPool : wishPools) {
            if (wishPool.type == GenshinWishPoolType.STANDARD) {
                standardPool = (StandardPool) wishPool;
                break;
            }
        }
        assert standardPool != null;
        return standardPool;
    }

    public GenshinEventWishPool getEventPool(String poolId) {
        GenshinEventWishPool eventWishPool = null;
        for (GenshinWishPool wishPool : wishPools) {
            if (wishPool.wishPoolId.equals(poolId)) {
                eventWishPool = (GenshinEventWishPool) wishPool;
                break;
            }
        }
        assert eventWishPool != null;
        return eventWishPool;
    }

}
