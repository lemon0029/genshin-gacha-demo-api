package me.xuqu.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.xuqu.model.entity.item.GenshinItem;
import lombok.Data;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 6:38 PM
 */
@Data
public class GenshinWishDTO {

    @JsonProperty("wish_gifts")
    private List<GenshinItem> wishGifts;

    @JsonProperty("wish_statistic")
    private GenshinWishStatisticDTO genshinWishStatisticDTO;

}
