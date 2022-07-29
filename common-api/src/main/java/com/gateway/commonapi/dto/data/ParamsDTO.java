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
@Schema(name="ParamsDTO",description =  "Information about Params")
public class ParamsDTO {

	@Schema(name = "paramsId",
			description = "id Params",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("paramsId")
	private UUID paramsId;

	@Schema(name = "key",
			description = "the name of params",
			example = "start_at")
	@JsonProperty("key")
	private String key;

	@Schema(name = "sensitive",
			description = "the sensitive field allows not to show a value of the template in the logs",
			example = "1")
	@JsonProperty("sensitive")
	private Integer sensitive;

	@Schema(name = "value",
			description = "value of the param",
			example = "null")
	@JsonProperty("value")
	private String value;

	@Schema(name = "keyMapper",
			description = "keyMapper",
			example = "ORIGIN_LATITUDE")
	@JsonProperty("keyMapper")
	private String keyMapper;

	@Schema(name = "precision",
			description = "the precision that will be applied to parameter according to the number of calls",
			example = "-15")
	@JsonProperty("precision")
	private String precision;

	@Schema(name = "timezone",
			description = "the timezone",
			example = "Europe/Paris")
	@JsonProperty("timezone")
	private String timezone;

}
