package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@JsonPropertyOrder({"passengerPickupDay", "passengerPickupTimeOfDay", "JourneySchedules"})
public class Schedule implements Serializable {

    @Schema(description = "Day of week of the passenger pick-up.\n" +
            "ENUM \"MON, TUE, WED, THU, FRI, SAT, SUN\"",
            example = "MON")
    @JsonProperty("passengerPickupDay")
    private Day passengerPickupDay;

    @Schema(description = "Passenger pick-up time of day represented as RFC3339 partial-time",
            example = "7:30:00 AM")
    @JsonProperty("passengerPickupTimeOfDay")
    private String passengerPickupTimeOfDay;

    @Schema(name = "JourneySchedules")
    @JsonProperty("JourneySchedules")
    private List<JourneySchedule> journeySchedules;


}
