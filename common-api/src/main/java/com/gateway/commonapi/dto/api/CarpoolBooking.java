package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CarpoolBooking
 */
@Validated
@Data
public class CarpoolBooking   {
    @JsonProperty("id")
    @Schema(required = true, example = "booking00321")
    @NotNull
    private String id = null;

    @Schema(required = true, example = "1657092126")
    @JsonProperty("passengerPickupDate")
    @NotNull
    private BigDecimal passengerPickupDate = null;

    @JsonProperty("passengerPickupLat")
    @Schema(required = true, example = "2.293444")
    @NotNull
    private Double passengerPickupLat = null;

    @JsonProperty("passengerPickupLng")
    @Schema(required = true, example = "48.858997")
    @NotNull
    private Double passengerPickupLng = null;

    @JsonProperty("passengerDropLat")
    @Schema(required = true, example = "1.443332")
    @NotNull
    private Double passengerDropLat = null;

    @JsonProperty("passengerDropLng")
    @Schema(required = true, example = "43.604583")
    @NotNull
    private Double passengerDropLng = null;

    @JsonProperty("passengerPickupAddress")
    @Schema(example = "Pont d'Iéna, Paris")
    private String passengerPickupAddress = null;

    @JsonProperty("passengerDropAddress")
    @Schema(example = "Place du Capitole, Toulouse")
    private String passengerDropAddress = null;

    @JsonProperty("status")
    @Schema(required = true, example = "WAITING_CONFIRMATION")
    @NotNull
    private StatusEnum status = null;

    @JsonProperty("distance")
    @Schema(example = "700000")
    private Double distance = null;

    @JsonProperty("duration")
    @Schema(example = "700000")
    private Double duration = null;

    @JsonProperty("webUrl")
    @Schema(required = true, example = "carpool.mycity.com/booking/booking00321")
    @NotNull
    private String webUrl = null;


}
