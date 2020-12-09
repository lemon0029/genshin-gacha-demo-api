package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.entity.gacha.GenshinGachaPoolInfo;
import me.yec.model.support.wishpool.CharacterEventPool;
import me.yec.model.support.wishpool.StandardPool;
import me.yec.model.support.wishpool.WeaponEventPool;
import me.yec.repository.GenshinGachaPoolInfoRepository;
import me.yec.service.LotteryUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 抽奖用户业务实现类
 *
 * @author yec
 * @date 12/6/20 6:52 PM
 */
@Service
@RequiredArgsConstructor
public class LotteryUserServiceImpl implements LotteryUserService {

    private final GenshinGachaPoolInfoRepository genshinGachaPoolInfoRepository;

    /**
     * 初始化抽奖对象
     *
     * @return 抽奖对象
     */
    @Override
    public LotteryUser init() {
        LotteryUser lotteryUser = new LotteryUser();
        // 从数据库中拉取所有的抽奖池
        List<GenshinGachaPoolInfo> gachaPoolInfos = genshinGachaPoolInfoRepository.findAll();
        gachaPoolInfos.forEach(poolInfo -> {
            // 获取抽奖池的ID
            String gachaId = poolInfo.getId();
            // 根据奖池类型添加对应的对象到抽奖对象的奖池列表
            if (poolInfo.getGachaType() == 200) { // 常驻池
                StandardPool standardPool = new StandardPool();
                standardPool.wishPoolId = gachaId;
                lotteryUser.wishPools.add(standardPool);
            } else if (poolInfo.getGachaType() == 301) { // 角色活动池
                CharacterEventPool characterEventPool = new CharacterEventPool();
                characterEventPool.wishPoolId = gachaId;
                lotteryUser.wishPools.add(characterEventPool);
            } else if (poolInfo.getGachaType() == 302) { // 武器活动池
                WeaponEventPool weaponEventPool = new WeaponEventPool();
                weaponEventPool.wishPoolId = gachaId;
                lotteryUser.wishPools.add(weaponEventPool);
            }
        });
        return lotteryUser;
    }
}
