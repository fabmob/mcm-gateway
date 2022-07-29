package com.gateway.commonapi.dto.requestrelay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.boot.jackson.JsonComponent;

import java.util.Set;
import java.util.UUID;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MspCallsFinalDTO {

	@JsonIgnore
	private UUID mspCallId;

	@JsonIgnore
	private UUID mspActionId;

	@Schema(name = "url",
			description = "the call's url",
			example = "https://api.jcdecaux.com/vls/v1/stations",
			required = true)
	@JsonProperty("url")
	private String url;

	@Schema(name = "method",
			description = "the http method for the call",
			example = "GET",
			required = true)
	@JsonProperty("method")
	private String method;


	@Schema(name = "body",
			description = "the call's body",
			example = "{\"email\": \"test@gmail.com\"}")
	@JsonProperty("body")
	private String body;

	@Schema(name = "headers",
			description = "set of headers of the call")
	@JsonProperty("headers")
	private Set<HeadersValuesTemplateFinalDTO> headers;


}
