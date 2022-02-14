package me.xuqu.service;

import me.xuqu.model.dto.GenshinGachaPoolDTO;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 6:12 PM
 */
public interface GenshinGachaPoolService {
    List<GenshinGachaPoolDTO> findAllGachaPool();
}
