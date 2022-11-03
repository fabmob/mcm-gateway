package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * PassengerCarpoolBooking
 */
@Validated
@Data
@JsonDeserialize(using = JsonDeserializer.None.class)
public class PassengerCarpoolBooking extends CarpoolBooking implements OneOfCarpoolBookingEventData {
    @JsonProperty("passenger")
    private User passenger = null;

}