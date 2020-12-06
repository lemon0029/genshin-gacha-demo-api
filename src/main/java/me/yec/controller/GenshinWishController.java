package me.yec.controller;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.dto.GenshinWishDTO;
import me.yec.model.dto.GenshinWishStatisticDTO;
import me.yec.model.support.Result;
import me.yec.service.GenshinWishService;
import me.yec.service.LotteryUserService;
import me.yec.service.SimpleAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yec
 * @date 12/6/20 6:37 PM
 */
@RestController("/api/wish")
@RequiredArgsConstructor
public class GenshinWishController {

    private final GenshinWishService genshinWishService;
    private final LotteryUserService lotteryUserService;
    private final SimpleAuthService simpleAuthService;

    @GetMapping("/statistic")
    public Result<GenshinWishStatisticDTO> findStatisticByPoolId(@RequestParam(name = "poolId") String poolId,
                                                                 @RequestParam(name = "sessionId") String sessionId) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(sessionId);
        GenshinWishStatisticDTO byPoolId = genshinWishService.findStatisticByPoolId(poolId, currentUser);
        return Result.ok(byPoolId);
    }

    @GetMapping("/wish")
    public Result<GenshinWishDTO> wish(@RequestParam(name = "n") String n,
                                       @RequestParam(name = "poolId") String poolId,
                                       @RequestParam(name = "sessionId") String sessionId) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(sessionId);
        return null;
    }

}
