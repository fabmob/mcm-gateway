package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * DriverCarpoolBooking
 */
@Validated
@Data
@JsonDeserialize(using = JsonDeserializer.None.class)
public class DriverCarpoolBooking extends CarpoolBooking implements OneOfCarpoolBookingEventData {
    @JsonProperty("driver")
    @Schema(required = true, example = "\"driver\": {\n" +
            "      \"id\": \"user987654\",\n" +
            "      \"operator\": \"'carpool.mycity.com\",\n" +
            "      \"alias\": \"JeanD\",\n" +
            "      \"firstName\": \"Jean\",\n" +
            "      \"lastName\": \"Dupond\",\n" +
            "      \"grade\": 4,\n" +
            "      \"picture\": \"carpool.mycity.com/user/user987654/picture.jpg\",\n" +
            "      \"gender\": \"F\",\n" +
            "      \"verifiedIdentity\": true\n" +
            "    },")
    @NotNull
    private User driver = null;

    @JsonProperty("price")
    @NotNull
    @Valid
    @Schema(required = true, example = "\"type\": \"PAYING\",\n" +
            "      \"amount\": 15.3,\n" +
            "      \"currency\": \"EUR\"")
    private Price price = null;

    @JsonProperty("car")
    @Schema(example = "\"model\": \"Model Y\",\n" +
            "      \"brand\": \"Tesla\"")
    private Car car = null;
}