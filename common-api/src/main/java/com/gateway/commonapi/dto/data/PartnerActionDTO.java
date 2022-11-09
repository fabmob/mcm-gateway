package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;

import java.util.UUID;

@JsonComponent
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PartnerActionDTO", description = "Information about partnerActions")
public class PartnerActionDTO {

    @Schema(name = "partnerActionId",
            description = "id partnerActionId",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("partnerActionId")
    private UUID partnerActionId;

    @Schema(name = "name",
            description = "name of MspMeta",
            example = "jcdecaux")
    @JsonProperty("name")
    private String name;

    @Schema(name = "action",
            description = "action's name",
            example = "car_ride_price_search_action")
    @JsonProperty("action")
    private ActionsEnum action;

    @Schema(name = "isAuthentication",
            description = "check if the action is an authentication action",
            example = "true")
    @JsonProperty("isAuthentication")
    private boolean isAuthentication;

    @Schema(name = "isRefreshAuthentication",
            description = "isRefreshAuthentication",
            example = "0")
    @JsonProperty("isRefreshAuthentication")
    private Integer isRefreshAuth;

    @Schema(name = "isPagination",
            description = "isPagination",
            example = "1")
    @JsonProperty("isPagination")
    private Integer isPagination;

    @Schema(name = "selector",
            description = "selector")
    @JsonProperty("selector")
    private SelectorDTO selector;


}
