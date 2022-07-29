package com.gateway.commonapi.dto.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


/**
 * Bean for details about an potential action.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Generic response")
public class GenericResponse implements Serializable {

    /**
     * Response.
     */
    @Schema(
            name = "body",
            description = "Response",
            example = "[{\"mspId\": \"b814c97e-df56-4651-ac50-11525537964f\",\"stationId\": \"'XX:Y:12345678\",\"name\": \"'Island Central\",\"coordinates\": {\"lng\": 6.169639,\"lat\": 0,\"alt\": 0},\"physicalAddress\": {\"streetAddress\": \"example street 18, 2nd floor, 18-B33\",\"street\": \"example street\",\"houseNumber\": 18,\"houseNumberAddition\": \"18-B33\",\"addressAdditionalInfo\": \"2nd floor\",\"areaReference\": \"Smallcity, Pinetree county\",\"city\": \"Smallcity\",\"province\": \"Pinetree county\",\"state\": \"Tree state\",\"postalCode\": \"TR55555\",\"country\": \"FR\"},\"crossStreet\": \"on the corner with Secondary Road\",\"regionId\": \"Region1\",\"rentalMethods\": \"CREDITCARD\"}]",
            required = false)
    @JsonProperty("body")
    private Object body;

}
