package me.yec.controller;

import lombok.RequiredArgsConstructor;
import me.yec.model.entity.item.GenshinCharacter;
import me.yec.model.entity.item.GenshinItem;
import me.yec.model.entity.item.GenshinWeapon;
import me.yec.model.support.Result;
import me.yec.service.GenshinItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
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
