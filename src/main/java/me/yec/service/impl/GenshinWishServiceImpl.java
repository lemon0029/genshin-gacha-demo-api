package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.core.WishOM;
import me.yec.exception.AppException;
import me.yec.model.dto.GenshinWishDTO;
import me.yec.model.dto.GenshinWishStatisticDTO;
import me.yec.model.entity.item.GenshinItem;
import me.yec.model.support.wishpool.GenshinWishPool;
import me.yec.service.GenshinItemService;
import me.yec.service.GenshinWishService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author yec
 * @date 12/6/20 6:44 PM
 */
@Service
@RequiredArgsConstructor
public class GenshinWishServiceImpl implements GenshinWishService {

    private final GenshinItemService genshinItemService;
    private final WishOM wishOM;

    @Override
    public GenshinWishStatisticDTO findStatisticByPoolId(String poolId, LotteryUser lotteryUser) {
        GenshinWishPool wishPool = getGenshinWishPool(poolId, lotteryUser);
        return GenshinWishStatisticDTO.getInstance(wishPool);
    }

    @Override
    public GenshinWishDTO wishByPoolId(int n, String poolId, LotteryUser lotteryUser) {
        GenshinWishDTO genshinWishDTO = new GenshinWishDTO();

        // 使用 WishOM 抽取 GenshinItem 的 Ids
        wishOM.setLotteryUser(lotteryUser);
        List<Long> giftIds = wishOM.wishByPoolId(poolId, n);

        // 也许有可能出现这种情况
        if (giftIds == null) {
            throw new AppException("抽奖结果集异常");
        }
        // 根据指定的 Ids 获取 GenshinItem 列表
        List<GenshinItem> giftById = genshinItemService.findGiftById(giftIds);
        genshinWishDTO.setWishGifts(giftById);

        // 根据池子ID获取当前用户的对应池子对象
        GenshinWishPool wishPool = getGenshinWishPool(poolId, lotteryUser);

        // 初始化当前池子统计对象
        GenshinWishStatisticDTO instance = GenshinWishStatisticDTO.getInstance(wishPool);
        genshinWishDTO.setGenshinWishStatisticDTO(instance);
        return genshinWishDTO;
    }

    private GenshinWishPool getGenshinWishPool(String poolId, LotteryUser lotteryUser) {
        Optional<GenshinWishPool> genshinWishPoolOptional = lotteryUser.wishPools
                .stream()
                .filter(pool -> pool.wishPoolId.equals(poolId)).findFirst();

        return genshinWishPoolOptional
                .orElseThrow(() -> new AppException(String.format("gacha pool[%s] not found", poolId)));
    }


}
