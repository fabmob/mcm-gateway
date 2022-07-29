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
@Schema(name="AdaptersDTO",description =  "Information about Adaptes")
public class AdaptersDTO {

    @Schema(name = "adapterId",
            description = "Adapter identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            required = true)
    @JsonProperty("adapterId")
    private UUID adapterId;

    @Schema(name = "adapterName",
            description = "Adapter Name's",
            example = "default-adapter",
            required = true)
    @JsonProperty("adapterName")
    private String adapterName;
}
