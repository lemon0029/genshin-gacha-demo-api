package me.yec.controller;

import lombok.RequiredArgsConstructor;
import me.yec.model.dto.GenshinGachaPoolDTO;
import me.yec.model.support.Result;
import me.yec.service.GenshinGachaPoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 6:10 PM
 */
@RestController
@RequestMapping("/api/genshin")
@RequiredArgsConstructor
public class GenshinGachaPoolController {

    private final GenshinGachaPoolService genshinGachaPoolService;

    @GetMapping("/pool")
    public Result<List<GenshinGachaPoolDTO>> findAllGachaPool() {
        return Result.ok(genshinGachaPoolService.findAllGachaPool());
    }
}
