package me.yec.model.support.wishpool;

/**
 * 原神活动卡池
 *
 * @author yec
 * @date 12/4/20 10:46 PM
 */
public class GenshinEventWishPool extends GenshinWishPool {

    private static final long serialVersionUID = -6033630163466393445L;

    public boolean lastGet4isUp = false; // 上一个4星是否为UP角色
    public boolean lastGet5isUp = false; // 上一个5星是否为UP角色
    public int totalGet4UpCount = 0; // 总共获得4星UP个数
    public int totalGet5UpCount = 0; // 总共获得5星UP个数
}
