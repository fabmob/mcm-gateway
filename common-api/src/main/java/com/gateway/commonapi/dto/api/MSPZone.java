package com.gateway.commonapi.dto.api;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ZoneStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Data reprensenting an MSP geographical area")
public class MSPZone implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MSP identifier.
     */
    @Schema(
            name = "mspId",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            description = "Identifier of the MSP",
            required = true)
    @JsonProperty("mspId")
    @NotNull
    private UUID mspId;

    /**
     * MSP.
     */
    @Schema(
            name = "msp",
            description = "MSP name",
            required = true,
            example = "Name of MSP")
    @JsonProperty("msp")
    private String msp;

    /**
     * Status.
     */
    @Schema(
            name = "status",
            description = "Geographical zone status",
            required = true,
            example = "AVAILABLE",
            allowableValues = "AVAILABLE, UNAVAILABLE, NO_ZONE")
    @JsonProperty("status")
    private ZoneStatus status;

    /**
     * Last update.
     */
    @Schema(
            name = "updateDate",
            description = "Last update date",
            required = true,
            example = "2022-03-01T14:59:57.415Z")
    @JsonProperty("updateDate")
    private ZonedDateTime updateDate;

    /**
     * “FeatureCollection” (as per IETF RFC 7946).
     */
    @Schema(
            name = "type",
            description = "“FeatureCollection” (as per IETF RFC 7946)",
            example = "FeatureCollection")
    @JsonProperty("type")
    private String type;

    /**
     * Geographical areas.
     */
    @Schema(
            name = "zones",
            description = "List of geographical areas")
    @JsonProperty("zones")
    private List<Zone> zones;

    /**
     * Constructor.
     *
     * @param mspId MSP identifier.
     */
    public MSPZone(UUID mspId) {
        this(mspId, null, ZoneStatus.NO_ZONE);
    }

    /**
     * Constructor.
     *
     * @param mspId   MSP identifier.
     * @param mspName MSP name.
     * @param status  Status of the zone.
     */
    public MSPZone(UUID mspId, String mspName, ZoneStatus status) {
        this.mspId = mspId;
        this.msp = mspName;
        this.status = status;
    }

}

