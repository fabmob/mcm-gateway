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
@Schema(name = "PartnerStandardDTO", description = "Information about partnerStandard")
public class PartnerStandardDTO {

    @Schema(name = "partnerStandardId",
            description = "partnerStandardId identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("partnerStandardId")
    private UUID partnerStandardId;

    @Schema(name = "standardName",
            description = "Standard followed by the mspMeta",
            example = "FNMS")
    @JsonProperty("standardName")
    private String standardName;

    @Schema(name = "isActive",
            description = "If the current version is active or not",
            example = "true")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Schema(name = "versionStandard",
            description = "Standard Version",
            example = "V1.x")
    @JsonProperty("versionStandard")
    private String versionStandard;

    @Schema(name = "versionDataMapping",
            description = "Datamapping Version",
            example = "V2.x")
    @JsonProperty("versionDataMapping")
    private String versionDataMapping;

    @Schema(name = "partnerId",
            description = "partner Id",
            example = "b814c97e-df56-4651-ac50-11525537964f")
    @JsonProperty("partnerId")
    private UUID partnerId;

    @Schema(name = "partnerActionsId",
            description = "partnerAction Id",
            example = "fffe3671-627d-4c24-9fba-d39de25560bf")
    @JsonProperty("partnerActionsId")
    private UUID partnerActionsId;

    @Schema(name = "adaptersId",
            description = "Adapters Id",
            example = "80e3e8e3-cbd2-4b48-abee-8654afbaef88")
    @JsonProperty("adaptersId")
    private UUID adaptersId;

}
