package me.xuqu.controller;

import me.xuqu.model.entity.item.GenshinCharacter;
import me.xuqu.model.entity.item.GenshinItem;
import me.xuqu.model.entity.item.GenshinWeapon;
import me.xuqu.model.support.Result;
import me.xuqu.service.GenshinItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物品控制器
 * - 所有物品信息接口
 * - 武器信息接口
 * - 角色信息接口
 *
 * @author yec
 * @date 12/4/20 9:34 PM
 */
@RestController
@RequestMapping("/api/genshin")
@RequiredArgsConstructor
public class GenshinItemController {

    private final GenshinItemService genshinItemService;

    @GetMapping("/items")
    public Result<List<? extends GenshinItem>> findAllGenshinItem(@PageableDefault(size = 1000) Pageable pageable) {
        List<? extends GenshinItem> allGenshinItem = genshinItemService.findAllGenshinItem();
        return Result.ok(allGenshinItem);
    }

    @GetMapping("/characters")
    public Result<List<GenshinCharacter>> findAllGenshinCharacter(@PageableDefault(size = 1000) Pageable pageable) {
        List<GenshinCharacter> genshinCharacters = genshinItemService.findAllGenshinCharacter(pageable);
        return Result.ok(genshinCharacters);
    }

    @GetMapping("/weapons")
    public Result<List<GenshinWeapon>> findAllGenshinWeapon(@PageableDefault(size = 1000) Pageable pageable) {
        List<GenshinWeapon> genshinWeapons = genshinItemService.findAllGenshinWeapon(pageable);
        return Result.ok(genshinWeapons);
    }

}
