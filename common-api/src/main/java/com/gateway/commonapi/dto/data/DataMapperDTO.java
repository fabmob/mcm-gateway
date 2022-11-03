package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.util.UUID;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DataMapperDTO", description = "Information about DataMapper")
public class DataMapperDTO {

    @Schema(name = "dataMapperId",
            description = "id Data Mapper",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("dataMapperId")
    private UUID dataMapperId;

    @Schema(name = "internalField",
            description = "Internal field",
            example = "VEHICLE_CLASS")
    @JsonProperty("internalField")
    private String internalField;

    @Schema(name = "externalField",
            description = "external field",
            example = "localized_product_name")
    @JsonProperty("externalField")
    private String externalField;

    @Schema(name = "isArray",
            description = "the response is an array",
            example = "1")
    @JsonProperty("isArray")
    private Integer isArray;

    @Schema(name = "format",
            description = "formula to apply on field's value ",
            example = "NUMERIC_OPERATOR(*,1000)")
    @JsonProperty("format")
    private String format;

    @Schema(name = "timezone",
            description = "Localization / Offset used by the MSP",
            example = "Europe/Paris")
    @JsonProperty("timezone")
    private String timezone;

    @Schema(name = "containedValue",
            description = "if isArray=1, name of the field corresponding to 'internalField' in the array",
            example = "BabySeat")
    @JsonProperty("containedValue")
    private String containedValue;

    @Schema(name = "defaultValue",
            description = "defaultValue of 'internalField' used if 'externalField' is null")
    @JsonProperty("defaultValue")
    private String defaultValue;

    @Schema(name = "partnerActionId",
            description = "partnerActionId",
            example = "b814c97e-df56-4651-ac50-11525537964f")
    @JsonProperty("partnerActionId")
    private UUID partnerActionId;


}
