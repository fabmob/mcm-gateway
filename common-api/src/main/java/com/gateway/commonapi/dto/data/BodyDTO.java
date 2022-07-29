package com.gateway.commonapi.dto.data;

import java.util.Set;
import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="BodyDTO",description =  "Information about body")
public class BodyDTO {
	@Schema(name = "bodyId",
			description = "Body identifier",
			example = "b814c97e-df56-4651-ac50-11525537964f",
			required = true)
	@JsonProperty("bodyId")
	private UUID bodyId;

	@Schema(name = "template",
			description = "the body template",
			example = "{\"bike\":\"${VEHICULE_ID}\"}",
			required = true)
	@JsonProperty("template")
	private String template;

	@Schema(name = "isStatic",
			description = "isStatic",
			example = "1")
	@JsonProperty("isStatic")
	private Integer isStatic;

	@Schema(name = "bodyParams",
			description = "Fields and values of the body")
	@JsonProperty("bodyParams")
	private Set<BodyParamsDTO> bodyParams;
}
