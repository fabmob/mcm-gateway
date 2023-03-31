package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@Validated

public class CarpoolBookingEvent   {
    @JsonProperty("id")
    @Schema(required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID id = null;

    @JsonProperty("idToken")
    @Schema(description = "ID token of the user retrieved using the OpenID Connect flows.", required = true, example = "stringtokenid")
    @NotNull
    private String idToken = null;

    @JsonProperty("data")
    @Schema(required = true, example = "{\n" +
            "        \"id\": \"booking00321\",\n" +
            "        \"passengerPickupDate\": 1657092126,\n" +
            "        \"passengerPickupLat\": 2.293444,\n" +
            "        \"passengerPickupLng\": 48.858997,\n" +
            "        \"passengerDropLat\": 1.443332,\n" +
            "        \"passengerDropLng\": 43.604583,\n" +
            "        \"passengerPickupAddress\": \"Pont d'Iéna, Paris\",\n" +
            "        \"passengerDropAddress\": \"Place du Capitole, Toulouse\",\n" +
            "        \"status\": \"WAITING_CONFIRMATION\",\n" +
            "        \"distance\": 700000,\n" +
            "        \"duration\": 700001,\n" +
            "        \"webUrl\": \"carpool.mycity.com/booking/booking00321\",\n" +
            "        \"driver\": {\n" +
            "            \"id\": \"user987654\",\n" +
            "            \"operator\": \"'carpool.mycity.com\",\n" +
            "            \"alias\": \"JeanD\",\n" +
            "            \"firstName\": \"Jean\",\n" +
            "            \"lastName\": \"Dupond\",\n" +
            "            \"grade\": 4,\n" +
            "            \"picture\": \"carpool.mycity.com/user/user987654/picture.jpg\",\n" +
            "            \"gender\": \"F\",\n" +
            "            \"verifiedIdentity\": true\n" +
            "        },\n" +
            "        \"price\": {\n" +
            "            \"type\": \"PAYING\",\n" +
            "            \"amount\": 15.3,\n" +
            "            \"currency\": \"EUR\"\n" +
            "        },\n" +
            "        \"car\": {\n" +
            "            \"model\": \"Model Y\",\n" +
            "            \"brand\": \"Tesla\"\n" +
            "        }\n" +
            "    }")
    @NotNull
    @Valid
    private OneOfCarpoolBookingEventData data = null;

}
