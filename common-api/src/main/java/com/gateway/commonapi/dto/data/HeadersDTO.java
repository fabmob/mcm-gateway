package com.gateway.commonapi.dto.data;

import java.util.Set;
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
@Schema(name="HeadersDTO",description =  "Information about Headers")
public class HeadersDTO {

	@Schema(name = "headersId",
			description = "id Headers",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("headersId")
	private UUID headersId;

	@Schema(name = "key",
			description = "key of the header",
			example = "Accept-Encoding")
	@JsonProperty("key")
	private String key;

	@Schema(name = "sensitive",
			description = "the sensitive field allows not to show a value of the template in the logs",
			example = "1")
	@JsonProperty("sensitive")
	private Integer sensitive;

	@Schema(name = "value",
			description = "value of the header",
			example = "gzip, deflate, br")
	@JsonProperty("value")
	private String value;

	@Schema(name = "processFunction",
			description = "the character string representing the function applied to the value",
			example = "BASE64")
	@JsonProperty("processFunction")
	private String processFunction;

	@Schema(name = "securityFlag",
			description = "securityFlag",
			example = "0")
	@JsonProperty("securityFlag")
	private Integer securityFlag;

	@Schema(name = "valuePrefix",
			description = "The prefix that preced the access token",
			example = "Bearer")
	@JsonProperty("valuePrefix")
	private String valuePrefix;

	@Schema(name = "valueTemplate",
			description = "valueTemplate",
			example = "{client_id}:{client_secret}")
	@JsonProperty("valueTemplate")
	private String valueTemplate;

	@Schema(name = "headersValuesTemplate",
			description = "headersValuesTemplate")
	@JsonProperty("headersValuesTemplate")
	private Set<HeadersValuesTemplateDTO> headersValuesTemplate;

}
