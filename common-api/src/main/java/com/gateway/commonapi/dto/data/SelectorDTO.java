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
@Schema(name="SelectorDTO",description =  "Information about Selector")
public class SelectorDTO {

	@Schema(name = "selectorId",
			description = "id Selector",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("selectorId")
	private UUID selectorId;

	@Schema(name = "key",
			description = "key of the searched field in the response",
			example = "stations")
	@JsonProperty("key")
	private String key;

	@Schema(name = "value",
			description = "value",
			example = "4")
	@JsonProperty("value")
	private String value;

}
