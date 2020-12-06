package me.yec.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.yec.model.entity.item.GenshinItem;

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
