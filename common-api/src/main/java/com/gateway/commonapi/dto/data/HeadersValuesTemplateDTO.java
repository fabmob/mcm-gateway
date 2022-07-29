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
@Schema(name="HeadersValuesTemplateDTO",description =  "Information about HeadersValuesTemplate")
public class HeadersValuesTemplateDTO {

	@Schema(name = "headersValuesTemplateId",
			description = "id Headers Values Template",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("headersValuesTemplateId")
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
