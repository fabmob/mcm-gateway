package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * Bean representing subscriptions of a car park.
 */
@Data
@AllArgsConstructor @NoArgsConstructor
@Validated
@Schema(description = "Subscriptions of a car park")
public class ParkingSubscriptions implements Serializable {
    

    /**
     * Subscription name.
     */
    @Schema(
            name="name",
            example="Paris - demonstration - 24/7",
            description="Name of subscription")
    @JsonProperty("name")
    private String name;

    @Schema(
            name="type",
            example="FullTime",
            description="Type of subscription")
    @JsonProperty("type")
    private String type;

    @Schema(
            name="monthlyPrice",
            example="70",
            description="Monthly price of subscription")
    @JsonProperty("monthlyPrice")
    private Long monthlyPrice;

    @Schema(
            name="firstMonthPrice",
            example="70",
            description="Monthly price of subscription")
    @JsonProperty("firstMonthPrice")
    private Long firstMonthPrice;

}
