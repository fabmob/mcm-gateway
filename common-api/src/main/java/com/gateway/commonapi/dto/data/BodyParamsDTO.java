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
@Schema(name="BodyParamsDTO",description =  "Information about BodyParams")
public class BodyParamsDTO {

	@Schema(name = "bodyParamsId",
			description = "Body Params identifier",
			example = "b814c97e-df56-4651-ac50-11525537964f"
			,required = true)
	@JsonProperty("bodyParamsId;")
	private UUID bodyParamsId;

	@Schema(name = "keyMapper",
			description = "keyMapper",
			example = "VEHICLE_ID")
	@JsonProperty("keyMapper")
	private String keyMapper;

	@Schema(name = "key",
			description = "the name of param field in the original body",
			example = "CityID")
	@JsonProperty("key")
	private String key;

	@Schema(name = "sensitive",
			description = "the sensitive field allows not to show a value of the template in the logs",
			example = "1")
	@JsonProperty("sensitive")
	private Integer sensitive;

	@Schema(name = "value",
			description = "value of the body param",
			example = "96000")
	@JsonProperty("value")
	private String value;

	@Schema(name = "precision",
			description = "Enlarge the search time (in minutes)",
			example = "-15")
	@JsonProperty("precision")
	private String precision;

	@Schema(name = "timezone",
			description = "the timezone",
			example = "Europe/Paris")
	@JsonProperty("timezone")
	private String timezone;

	@Schema(name = "isRefreshToken",
			description = "isRefreshToken",
			example = "0")
	@JsonProperty("isRefreshToken")
	private Integer isRefreshToken;
}
