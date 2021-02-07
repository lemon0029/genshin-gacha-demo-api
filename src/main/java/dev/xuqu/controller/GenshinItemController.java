package dev.xuqu.controller;

import dev.xuqu.model.entity.item.GenshinCharacter;
import dev.xuqu.model.entity.item.GenshinItem;
import dev.xuqu.model.entity.item.GenshinWeapon;
import dev.xuqu.model.support.Result;
import dev.xuqu.service.GenshinItemService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/item")
    public Result<List<? extends GenshinItem>> findAllGenshinItem(
            @RequestParam(name = "type", required = false, defaultValue = "null") String type,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<? extends GenshinItem> allGenshinItem = genshinItemService.findAllGenshinItem(type, sort, order);
        return Result.ok(allGenshinItem);
    }

    @GetMapping("/character")
    public Result<List<GenshinCharacter>> findAllGenshinCharacter(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<GenshinCharacter> genshinCharacters = genshinItemService.findAllGenshinCharacter(sort, order);
        return Result.ok(genshinCharacters);
    }

    @GetMapping("/weapon")
    public Result<List<GenshinWeapon>> findAllGenshinWeapon(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<GenshinWeapon> genshinWeapons = genshinItemService.findAllGenshinWeapon(sort, order);
        return Result.ok(genshinWeapons);
    }

}
