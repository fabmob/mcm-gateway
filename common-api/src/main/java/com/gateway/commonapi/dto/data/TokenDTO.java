package com.gateway.commonapi.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="TokenDTO",description =  "Information about Token")
public class TokenDTO {
	@Schema(name = "tokenId", 
			description = "Token identifier", 
			example = "b814c97e-df56-4651-ac50-11525537964f",
			accessMode = Schema.AccessMode.READ_ONLY,
			required = true)
	@JsonProperty("tokenId")
	private UUID tokenId;

	@Schema(name = "accessToken",
			description = "value of the access token for the msp",
			example = "89e3dba6-7cb2-43cc-aea4-a804beecdea")
	@JsonProperty("accessToken")
	private String accessToken;
	
	@Schema(description = "expireAt",
			type = "string", 
			example = "2020-04-28T00:00:00.000Z")
	@JsonProperty("expireAt")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime expireAt;

	@Schema(name = "mspId",
			description = "mspMeta Id",
			example = "b814c97e-df56-4651-ac50-11525537964f")
	@JsonProperty("mspId")
	private UUID mspId;
}
