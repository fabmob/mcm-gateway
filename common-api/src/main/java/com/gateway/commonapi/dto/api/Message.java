package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.CarpoolerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Message {


    @Schema(name = "from",
            required = true,
            description = "Description of the sender of the message")
    @JsonProperty("from")
    @NotNull
    private User from;

    @Schema(name = "to",
            required = true,
            description = "Description of the recipient of the message")
    @JsonProperty("to")
    @NotNull
    private User to;

    @Schema(name = "message",
            required = true,
            maxLength = 500,
            example = "Hello, please contact me to talk about the journey. Regards.",
            description = "Free text content of a message. The message can contain all the details (phone number, email, etc.) allowing the recipient to call back the sender in order to carpool with him/her.")
    @JsonProperty("message")
    @NotNull
    private String message;

    @Schema(name = "recipientCarpoolerType",
            required = true,
            example = "DRIVER",
            defaultValue = "DRIVER",
            description = "Defines if the recipient of this message is either the driver or the passenger.")
    @JsonProperty("recipientCarpoolerType")
    @NotNull
    private CarpoolerType recipientCarpoolerType;

    @Schema(name = "driverJourneyId",
            example = "Journey00001",
            minLength = 1,
            maxLength = 255,
            description = "ID of the Driver's journey to which the message is related (if any). Unique given the Driver's operator property.")
    @JsonProperty("driverJourneyId")
    private String driverJourneyId;

    @Schema(name = "passengerJourneyId",
            example = "Journey00435",
            minLength = 1,
            maxLength = 255,
            description = "ID of the Passenger's journey to which the message is related (if any). Unique given the Passenger's operator property.")
    @JsonProperty("passengerJourneyId")
    private String passengerJourneyId;

    @Schema(name = "bookingId",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            description = "ID of the booking to which the message is related (if any)")
    @JsonProperty("bookingId")
    private UUID bookingId;
}
