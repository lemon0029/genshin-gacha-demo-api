package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.model.dto.GenshinGachaPoolDTO;
import me.yec.model.entity.gacha.GenshinGachaPool;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.repository.GenshinGachaPoolRepository;
import me.yec.service.GenshinGachaPoolService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yec
 * @date 12/6/20 6:13 PM
 */
@Service
@RequiredArgsConstructor
public class GenshinGachaPoolServiceImpl implements GenshinGachaPoolService {

    private final GenshinGachaPoolRepository genshinGachaPoolRepository;
    private final GenshinGachaPoolInfoRepository genshinGachaPoolInfoRepository;

    @Override
    public List<GenshinGachaPoolDTO> findAllGachaPool() {
        List<GenshinGachaPoolDTO> gachaPoolDTOS = new ArrayList<>();
        List<GenshinGachaPool> pools = genshinGachaPoolRepository.findAll(Sort.by(Sort.Direction.DESC, "beginTime"));
        pools.forEach(gachaPool -> {
            GenshinGachaPoolDTO gachaPoolDTO = new GenshinGachaPoolDTO();
            Optional<GenshinGachaPoolInfo> gachaPoolInfoOptional = genshinGachaPoolInfoRepository.findById(gachaPool.getId());
            if (gachaPoolInfoOptional.isPresent()) {
                GenshinGachaPoolInfo gachaPoolInfo = gachaPoolInfoOptional.get();
                gachaPoolDTO.setGachaPoolId(gachaPool.getId());
                gachaPoolDTO.setGachaPoolTitle(gachaPoolInfo.getNickTitle());
//                gachaPoolDTO.setGachaPoolName(gachaPool.getName());
            }
            gachaPoolDTOS.add(gachaPoolDTO);
        });
        return gachaPoolDTOS;
    }
}
