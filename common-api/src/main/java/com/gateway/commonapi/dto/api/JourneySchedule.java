package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.TypeEnumDriver;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class JourneySchedule {

    @Schema(description = "Journey's id. It MUST be unique for a given operator.",
            example = "journey012345", minLength = 1, maxLength = 255)
    @JsonProperty("id")
    private String id;

    @Schema(required = true,
            description = "Passenger pickup datetime as a UNIX UTC timestamp in seconds.",
            example = "1655452466")
    @JsonProperty("passengerPickupDate")
    private Integer passengerPickupDate;

    @Schema(description = "Driver departure datetime as a UNIX UTC timestamp in seconds.",
            example = "1655452466")
    @JsonProperty("driverDepartureDate")
    private Integer driverDepartureDate;

    @Schema(description = "URL of the journey on the webservice provider platform. Required to support booking by deeplink.",
            example = "carpool.mycity.com/journey/journey012345")
    @JsonProperty("webUrl")
    private String webUrl;

    @Schema(required = true,
            description = "Type of journey. A dynamic journey is happening in real time.\n" +
            "ENUM \"PLANNED, DYNAMIC, LINE\"",
            example = "PLANNED")
    @JsonProperty("type")
    private TypeEnumDriver type;
}
