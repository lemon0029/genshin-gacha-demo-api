package me.xuqu.model.dto;

import me.xuqu.model.support.wishpool.CharacterEventPool;
import me.xuqu.model.support.wishpool.GenshinWishPool;
import me.xuqu.model.support.wishpool.GenshinWishPoolType;
import me.xuqu.model.support.wishpool.WeaponEventPool;

/**
 * @author yec
 * @date 12/6/20 6:39 PM
 */
public class GenshinWishStatisticDTO {
    public String wishPoolId;
    public Integer lastGet4Count; // 上一次获得4星到现在抽了几次
    public Integer lastGet5Count; // 上一次获得5星到现在抽了几次
    public Integer get4ByBaodiCount; // 通过保底的方式获得4星的次数
    public Integer get5ByBaodiCount; // 通过保底的方式获得5星的次数
    public Integer totalGet4Count; // 总共获得4星角色多少
    public Integer totalGet5Count; // 总共获得4星角色多少
    public Integer totalWishCount; // 总共祈愿数
    //    public List<Long> wishResult; // 抽奖结果（存放角色或者武器的ID）
    public Boolean lastGet4isUp; // 上一个4星是否为UP角色
    public Boolean lastGet5isUp; // 上一个5星是否为UP角色
    public Integer totalGet4UpCount; // 总共获得4星UP个数
    public Integer totalGet5UpCount; // 总共获得5星UP个数

    /**
     * 获取一个统计DTO对象，通过给定的奖池初始化DTO的属性值
     *
     * @param wishPool 指定奖池
     * @return DTO对象
     */
    public static GenshinWishStatisticDTO getInstance(GenshinWishPool wishPool) {
        GenshinWishStatisticDTO statisticDTO = new GenshinWishStatisticDTO();
        statisticDTO.wishPoolId = wishPool.wishPoolId;
        statisticDTO.totalGet4Count = wishPool.totalGet4Count;
        statisticDTO.totalGet5Count = wishPool.totalGet5Count;
        statisticDTO.totalWishCount = wishPool.totalWishCount;
        statisticDTO.get4ByBaodiCount = wishPool.get4ByBaodiCount;
        statisticDTO.get5ByBaodiCount = wishPool.get5ByBaodiCount;
        statisticDTO.lastGet4Count = wishPool.lastGet4Count;
        statisticDTO.lastGet5Count = wishPool.lastGet5Count;
//        genshinWishPoolDTO.wishResult = wishPool.wishResult;

        if (wishPool.type == GenshinWishPoolType.CHARACTER_EVENT) {
            statisticDTO.lastGet4isUp = ((CharacterEventPool) wishPool).lastGet4isUp;
            statisticDTO.lastGet5isUp = ((CharacterEventPool) wishPool).lastGet5isUp;
            statisticDTO.totalGet4UpCount = ((CharacterEventPool) wishPool).totalGet4UpCount;
            statisticDTO.totalGet5UpCount = ((CharacterEventPool) wishPool).totalGet5UpCount;
        }

        if (wishPool.type == GenshinWishPoolType.WEAPON_EVENT) {
            statisticDTO.lastGet4isUp = ((WeaponEventPool) wishPool).lastGet4isUp;
            statisticDTO.lastGet5isUp = ((WeaponEventPool) wishPool).lastGet5isUp;
            statisticDTO.totalGet4UpCount = ((WeaponEventPool) wishPool).totalGet4UpCount;
            statisticDTO.totalGet5UpCount = ((WeaponEventPool) wishPool).totalGet5UpCount;
        }

        return statisticDTO;
    }

}
