package com.gateway.commonapi.dto.data;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="DataMapperDTO",description =  "Information about DataMapper")
public class DataMapperDTO {

	@Schema(name = "dataMapperId",
			description = "id Data Mapper",
			example = "b814c97e-df56-4651-ac50-11525537964f",
			accessMode = Schema.AccessMode.READ_ONLY,
			required = true)
    @JsonProperty( "dataMapperId")
	private UUID dataMapperId;

	@Schema(name = "champInterne",
			description = "Intern field",
			example = "VEHICLE_CLASS")
	@JsonProperty("champInterne")
	private String champInterne;

	@Schema(name = "champExterne",
			description = "extern field",
			example = "localized_product_name")
	@JsonProperty("champExterne")
	private String champExterne;

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
			description = "if isArray=1, name of the field corresponding to 'champ_interne' in the array",
			example = "BabySeat")
	@JsonProperty("containedValue")
	private String containedValue;

	@Schema(name = "defaultValue",
			description = "defaultValue of 'champInterne' used if 'champExterne' is null")
	@JsonProperty("defaultValue")
    private String defaultValue;

	@Schema(name = "mspActionId",
			description = "mspActionId",
			example = "b814c97e-df56-4651-ac50-11525537964f")
	@JsonProperty("mspActionId")
	private UUID mspActionId;


}
