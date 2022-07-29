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
			description = "champInterne",
			example = "VEHICLE_CLASS")
	@JsonProperty("champInterne")
	private String champInterne;

	@Schema(name = "champExterne",
			description = "champExterne",
			example = "localized_product_name")
	@JsonProperty("champExterne")
	private String champExterne;

	@Schema(name = "isArray",
			description = "isArray",
			example = "1")
	@JsonProperty("isArray")
	private Integer isArray;

	@Schema(name = "format",
			description = "format",
			example = "yyyy/M/d HH:mm:ss")
	@JsonProperty("format")
	private String format;

	@Schema(name = "timezone",
			description = "timezone",
			example = "Europe/Paris")
	@JsonProperty("timezone")
	private String timezone;

	@Schema(name = "containedValue",
			description = "containedValue",
			example = "Automatic")
	@JsonProperty("containedValue")
	private String containedValue;

	@Schema(name = "defaultValue",
			description = "defaultValue",
			example = "chauffeur-p://order?product={product_id}&price_token={price_token}&source=RATP_MaaS")
	@JsonProperty("defaultValue")
    private String defaultValue;

	@Schema(name = "mspActionId",
			description = "mspActionId",
			example = "b814c97e-df56-4651-ac50-11525537964f")
	@JsonProperty("mspActionId")
	private UUID mspActionId;


}
