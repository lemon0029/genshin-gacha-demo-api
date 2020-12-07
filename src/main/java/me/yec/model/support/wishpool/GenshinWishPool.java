package me.yec.model.support.wishpool;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 原神卡池
 *
 * @author yec
 * @date 12/4/20 10:39 PM
 */
@Data
public class GenshinWishPool implements Serializable {

    private static final long serialVersionUID = 4244075773388875279L;

    public GenshinWishPoolType type;
    public String wishPoolId;
    public int lastGet4Count = 0; // 上一次获得4星到现在抽了几次
    public int lastGet5Count = 0; // 上一次获得5星到现在抽了几次
    public int get4ByBaodiCount = 0; // 通过保底的方式获得4星的次数
    public int get5ByBaodiCount = 0; // 通过保底的方式获得5星的次数
    public int totalGet4Count = 0; // 总共获得4星角色多少
    public int totalGet5Count = 0; // 总共获得4星角色多少
    public int totalWishCount = 0; // 总共祈愿数
    public List<Long> wishInventory = new ArrayList<>(); // 抽奖结果（存放角色或者武器的ID）
}
