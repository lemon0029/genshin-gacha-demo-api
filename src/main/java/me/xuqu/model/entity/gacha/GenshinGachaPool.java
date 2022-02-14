package me.xuqu.model.entity.gacha;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author yec
 * @date 12/6/20 4:59 PM
 */
@Data
@Entity
@Table(name = "genshin_gacha_pool")
public class GenshinGachaPool {
    @Id
    private String id;
    private String name;
    private Integer type;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
