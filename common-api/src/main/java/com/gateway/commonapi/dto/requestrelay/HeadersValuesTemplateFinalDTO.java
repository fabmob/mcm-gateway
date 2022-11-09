package com.gateway.commonapi.dto.requestrelay;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HeadersValuesTemplateFinalDTO {


	@JsonIgnore
	private UUID headersValuesTemplateId;

	@Schema(name = "key",
			description = "key of the header",
			example = "Content-Type")
	@JsonProperty("key")
	private String key;

	@Schema(name = "value",
			description = "value of the header",
			example = "application/json")
	@JsonProperty("value")
	private String value;
}
