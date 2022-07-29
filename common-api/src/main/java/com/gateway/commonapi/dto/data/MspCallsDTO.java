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
@Schema(name = "MspCallsDTO", description = "Information about MspCalls")
public class MspCallsDTO {

	@Schema(name = "mspCallId",
			description = "MspCalls identifier", 
			example = "b814c97e-df56-4651-ac50-11525537964f",
			accessMode = Schema.AccessMode.READ_ONLY,
			required = true)
	@JsonProperty("mspCallId")
	private UUID mspCallId;

	@Schema(name = "url", 
			description = "the call's url",
			example = "https://api.jcdecaux.com/vls/v1/stations")
	@JsonProperty("url")
	private String url;

	@Schema(name = "method", 
			description = "the http method for the call",
			example = "GET")
	@JsonProperty("method")
	private String method;

	@Schema(name = "isMocked", 
			description = "isMocked", 
			example = "0")
	@JsonProperty("isMocked")
	private Integer isMocked;

	@Schema(name = "nbCalls", 
			description = "number of calls", 
			example = "12")
	@JsonProperty("nbCalls")
	private Integer nbCalls;
	
	@Schema(name = "executionOrder", 
			description = "the execution order", 
			example = "3")
	@JsonProperty("executionOrder")
	private Integer executionOrder;
	

	@Schema(name = "mspActionId", 
			description = "mspActionId", 
			example = "bd49261e-f036-441d-bee1-f7c3107a8ecd")
	@JsonProperty("mspActionId")
	private UUID mspActionId;

	@Schema(name = "body", 
			description = "the call's body")
	@JsonProperty("body")
	private BodyDTO body;

	@Schema(name = "headers", 
			description = "call's headers")
	@JsonProperty("headers")
	private Set<HeadersDTO> headers;

	@Schema(name = "params", 
			description = "call's params")
	@JsonProperty("params")
	private Set<ParamsDTO> params;

	@Schema(name = "paramsMultiCalls", 
			description = "params for multiple calls")
	@JsonProperty("paramsMultiCalls")
	private Set<ParamsMultiCallsDTO> paramsMultiCalls;
}
