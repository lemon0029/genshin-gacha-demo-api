package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 奖池业务实现类
 *
 * @author yec
 * @date 12/6/20 6:13 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenshinGachaPoolServiceImpl implements GenshinGachaPoolService {

    private final GenshinGachaPoolRepository genshinGachaPoolRepository;
    private final GenshinGachaPoolInfoRepository genshinGachaPoolInfoRepository;

    @Override
    public List<GenshinGachaPoolDTO> findAllGachaPool() {
        // 根据开启时间降序查询所有的 GachaPool
        List<GenshinGachaPool> pools = genshinGachaPoolRepository.findAll(Sort.by(Sort.Direction.DESC, "beginTime"));

        List<GenshinGachaPoolDTO> gachaPoolDTOS = new ArrayList<>();
        pools.forEach(gachaPool -> {
            GenshinGachaPoolDTO gachaPoolDTO = new GenshinGachaPoolDTO();
            // 根据 gacha_id 查询池子的信息（这个 id 是字符串类型）
            Optional<GenshinGachaPoolInfo> gachaPoolInfoOptional = genshinGachaPoolInfoRepository.findById(gachaPool.getId());
            if (gachaPoolInfoOptional.isPresent()) {

                // 初始化 gachaPoolDTO 对象
                GenshinGachaPoolInfo gachaPoolInfo = gachaPoolInfoOptional.get();
                gachaPoolDTO.setGachaPoolId(gachaPool.getId());
                gachaPoolDTO.setGachaPoolTitle(gachaPoolInfo.getNickTitle());
//                gachaPoolDTO.setGachaPoolName(gachaPool.getName());
            } else {
                // 只要不是手动更改或添加 GachaPool，对应的 GachaPoolInfo 一定存在
                // 一般情况不会出现这种情况，GachaPoolInfo 可以说是依赖于 GachaPool 存在
                log.warn("gacha pool[{}] not found", gachaPool.getId());
            }
            gachaPoolDTOS.add(gachaPoolDTO);
        });
        return gachaPoolDTOS;
    }
}
