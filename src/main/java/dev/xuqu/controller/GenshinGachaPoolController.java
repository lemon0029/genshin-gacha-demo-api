package dev.xuqu.controller;

import dev.xuqu.model.dto.GenshinGachaPoolDTO;
import dev.xuqu.model.support.Result;
import dev.xuqu.service.GenshinGachaPoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 奖池控制器
 *
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
