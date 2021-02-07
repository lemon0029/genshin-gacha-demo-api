package dev.xuqu.controller;

import dev.xuqu.core.LotteryUser;
import dev.xuqu.model.dto.GenshinWishDTO;
import dev.xuqu.model.dto.GenshinWishStatisticDTO;
import dev.xuqu.model.support.Result;
import dev.xuqu.service.GenshinWishService;
import dev.xuqu.service.SimpleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 祈愿控制类
 * - 祈愿接口
 * - 重置抽奖池接口
 * - 奖池信息统计接口
 *
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
    public Result<GenshinWishStatisticDTO> findStatisticByPoolId(
            @RequestParam(name = "poolId") String poolId, HttpServletRequest request) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(request.getHeader("vid"));
        GenshinWishStatisticDTO byPoolId = genshinWishService.findStatisticByPoolId(poolId, currentUser);
        return Result.ok(byPoolId);
    }

    @GetMapping("/reset")
    public Result<String> doRest(
            @RequestParam(name = "poolId") String poolId, HttpServletRequest request) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(request.getHeader("vid"));
        genshinWishService.reset(poolId, currentUser);
        // 重置之后更新用户数据到 redis 数据库...
        simpleAuthService.updateUser(request.getHeader("vid"), currentUser);
        return Result.ok("success");
    }

    @GetMapping
    public Result<GenshinWishDTO> wish(@RequestParam(name = "n", required = false, defaultValue = "10") int n,
                                       @RequestParam(name = "poolId") String poolId, HttpServletRequest request) {
        n = Math.min(90, n); // 最大九十次
        String vid = request.getHeader("vid");
        LotteryUser currentUser = simpleAuthService.getCurrentUser(vid);
        GenshinWishDTO genshinWishDTO = genshinWishService.wishByPoolId(n, poolId, currentUser);

        // 抽奖完之后更新用户数据到 redis 数据库...
        simpleAuthService.updateUser(vid, currentUser);
        return Result.ok(genshinWishDTO);
    }

}
