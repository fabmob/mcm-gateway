package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Bean representing availability of a car park.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Availability of a car park")
public class ParkingAvailability implements Serializable {


    @Schema(
            name="reservation",
            description="True if the reservation is available")
    @JsonProperty("reservation")
    @NotNull
    private Boolean reservation;
}

