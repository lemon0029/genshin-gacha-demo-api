package me.yec.controller;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.dto.GenshinInventoryDTO;
import me.yec.model.support.Result;
import me.yec.model.support.wishpool.GenshinWishPool;
import me.yec.service.GenshinItemService;
import me.yec.service.SimpleAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 抽奖仓库控制器
 *
 * @author yec
 * @date 12/7/20 9:05 PM
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class GenshinInventoryController {

    private final SimpleAuthService simpleAuthService;
    private final GenshinItemService genshinItemService;


    @GetMapping
    public Result<List<GenshinInventoryDTO>> findAllInInventory(
            @RequestParam(name = "poolId", required = false) String poolId,
            @RequestParam(name = "type", required = false, defaultValue = "null") String type,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order,
            HttpServletRequest request) {
        LotteryUser currentUser = simpleAuthService.getCurrentUser(request.getHeader("vid"));
        if (poolId == null) {
            poolId = currentUser.getStandardPool().wishPoolId;
        }
        GenshinWishPool poolById = currentUser.getPoolById(poolId);
        List<Long> wishInventory = poolById.wishInventory;
        List<GenshinInventoryDTO> allByIds = genshinItemService.findAllByIds(wishInventory, type, sort, order);
        return Result.ok(allByIds);
    }

}
