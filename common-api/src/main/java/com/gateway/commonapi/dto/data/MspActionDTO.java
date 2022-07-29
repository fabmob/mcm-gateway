package com.gateway.commonapi.dto.data;

import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonComponent
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="MspActionDTO",description =  "Information about MspActions")
public class MspActionDTO {

	@Schema(name = "mspActionId",
			description = "id MspAction",
			example = "b814c97e-df56-4651-ac50-11525537964f",
			accessMode = Schema.AccessMode.READ_ONLY,
			required = true)
	@JsonProperty("mspActionId")
	private UUID mspActionId;

	@Schema(name = "name",
			description = "name of MspMeta",
			example = "jcdecaux")
	@JsonProperty("name")
	private String name;

	@Schema(name = "action",
			description = "action's name",
			example = "car_ride_price_search_action")
	@JsonProperty("action")
	private String action;

	@Schema(name = "isAuthentication",
			description = "check if the action is an authentication action",
			example = "true")
	@JsonProperty("isAuthentication")
	private boolean isAuthentication ;

	@Schema(name = "isRefreshAuthentication",
			description = "isRefreshAuthentication",
			example = "0")
	@JsonProperty("isRefreshAuthentication")
	private Integer isRefreshAuth;

	@Schema(name = "isPagination",
			description = "isPagination",
			example = "1")
	@JsonProperty("isPagination")
	private Integer isPagination;

	@Schema(name = "selector",
			description = "selector")
	@JsonProperty("selector")
	private SelectorDTO selector;


}
