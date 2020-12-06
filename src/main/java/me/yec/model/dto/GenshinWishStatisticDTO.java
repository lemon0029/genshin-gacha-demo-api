package me.yec.model.dto;

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
}
