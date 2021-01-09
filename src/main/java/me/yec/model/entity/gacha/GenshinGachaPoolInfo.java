package me.yec.model.entity.gacha;

import lombok.Data;

import javax.persistence.*;

/**
 * @author yec
 * @date 12/6/20 5:11 PM
 */
@Data
@Entity
@Table(name = "genshin_gacha_pool_info")
public class GenshinGachaPoolInfo {
    @Id
    private String id;

    /* 现在的池子没有这个 gacha_id 了，直接用 id 作为唯一标识吧
    @Column(name = "gacha_id")
    private String gachaId; */

    private String title;
    @Column(name = "nick_title", updatable = false)
    private String nickTitle;
    @Column(name = "gacha_type")
    private Integer gachaType;

    @Lob
    private String content;

    @Column(name = "r5_up_prob")
    private Double r5UpProb;
    @Column(name = "r4_up_prob")
    private Double r4UpProb;
    @Column(name = "r5_prob")
    private Double r5Prob;
    @Column(name = "r4_prob")
    private Double r4Prob;
    @Column(name = "r3_prob")
    private Double r3Prob;
    @Column(name = "r5_baodi_prob")
    private Double r5BaodiProb;
    @Column(name = "r4_baodi_prob")
    private Double r4BaodiProb;
    @Column(name = "r3_baodi_prob")
    private Double r3BaodiProb;
}
