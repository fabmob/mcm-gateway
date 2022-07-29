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
@Schema(name="ParamsMultiCallsDTO",description =  "Information about ParamsMultiCalls")
public class ParamsMultiCallsDTO {

	@Schema(name = "paramsMultiCallsId",
			description = "id Params Multi Calls",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("paramsMultiCallsId")
	private UUID paramsMultiCallsId;

	@Schema(name = "key",
			description = "key Params Multi Calls",
			example = "fel_key")
	@JsonProperty("key")
	private String key;

	@Schema(name = "valueOffset",
			description = "time between each call",
			example = "30")
	@JsonProperty("valueOffset")
	private String valueOffset;

	@Schema(name = "initValue",
			description = "initValue",
			example = "OFFSET")
	@JsonProperty("initValue")
	private String initValue;

	@Schema(name = "timezone",
			description = "timezone",
			example = "Europe/Paris")
	@JsonProperty("timezone")
	private String timezone;


}
