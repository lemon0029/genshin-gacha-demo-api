package me.yec.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author yec
 * @date 12/6/20 6:11 PM
 */
@Data
public class GenshinGachaPoolDTO {
    @JsonProperty("pool_id")
    private String gachaPoolId;
    @JsonProperty("pool_title")
    private String gachaPoolTitle;
}
