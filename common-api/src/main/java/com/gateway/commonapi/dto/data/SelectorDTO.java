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
@Schema(name="SelectorDTO",description =  "Information about Selector")
public class SelectorDTO {

	@Schema(name = "selectorId",
			description = "id Selector",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("selectorId")
	private UUID selectorId;

	@Schema(name = "key",
			description = "key",
			example = "Data")
	@JsonProperty("key")
	private String key;

	@Schema(name = "value",
			description = "value",
			example = "4")
	@JsonProperty("value")
	private String value;

}
