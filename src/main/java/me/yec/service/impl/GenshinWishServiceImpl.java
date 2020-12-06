package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.core.WishOM;
import me.yec.model.dto.GenshinWishDTO;
import me.yec.model.dto.GenshinWishStatisticDTO;
import me.yec.model.entity.item.GenshinItem;
import me.yec.service.GenshinItemService;
import me.yec.service.GenshinWishService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        GenshinWishStatisticDTO genshinWishStatisticDTO = new GenshinWishStatisticDTO();
        return null;
    }

    @Override
    public GenshinWishDTO wishByPoolId(int n, String poolId, LotteryUser lotteryUser) {
        GenshinWishDTO genshinWishDTO = new GenshinWishDTO();
        wishOM.setLotteryUser(lotteryUser);

        List<Long> giftIds = wishOM.wishByPoolId(poolId, n);
        List<GenshinItem> giftById = genshinItemService.findGiftById(giftIds);
        genshinWishDTO.setWishGifts(giftById);
        return genshinWishDTO;
    }


}
