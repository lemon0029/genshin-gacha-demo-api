package me.yec.controller;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.dto.GenshinWishDTO;
import me.yec.model.dto.GenshinWishStatisticDTO;
import me.yec.model.support.Result;
import me.yec.service.GenshinWishService;
import me.yec.service.SimpleAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yec
 * @date 12/6/20 6:37 PM
 */
@RestController
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class GenshinWishController {

    private final GenshinWishService genshinWishService;
    private final SimpleAuthService simpleAuthService;

    @GetMapping("/statistic")
    public Result<GenshinWishStatisticDTO> findStatisticByPoolId(@RequestParam(name = "poolId") String poolId,
                                                                 @RequestParam(name = "vid") String vid) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(vid);
        GenshinWishStatisticDTO byPoolId = genshinWishService.findStatisticByPoolId(poolId, currentUser);
        return Result.ok(byPoolId);
    }

    @GetMapping
    public Result<GenshinWishDTO> wish(@RequestParam(name = "n", required = false, defaultValue = "10") int n,
                                       @RequestParam(name = "poolId") String poolId,
                                       @RequestParam(name = "vid") String vid) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(vid);
        GenshinWishDTO genshinWishDTO = genshinWishService.wishByPoolId(n, poolId, currentUser);

        // 抽奖完之后更新用户数据到 redis 数据库...
        simpleAuthService.updateUser(vid, currentUser);
        return Result.ok(genshinWishDTO);
    }

}
