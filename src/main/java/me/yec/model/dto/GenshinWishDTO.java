package me.yec.model.dto;

import lombok.Data;
import me.yec.model.entity.item.GenshinItem;

import java.util.List;

/**
 * @author yec
 * @date 12/6/20 6:38 PM
 */
@Data
public class GenshinWishDTO {

    private List<GenshinItem> wishGifts;

}
