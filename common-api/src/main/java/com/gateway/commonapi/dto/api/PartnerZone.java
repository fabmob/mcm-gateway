package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ZoneStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Data representing a partner geographical area")
public class PartnerZone implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MSP identifier.
     */
    @Schema(
            name = "partnerId",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            description = "Identifier of the MSP",
            required = true)
    @JsonProperty("partnerId")
    @NotNull
    private UUID partnerId;

    /**
     * MSP.
     */
    @Schema(
            name = "partner",
            description = "partner name",
            required = true,
            example = "Name of partner")
    @JsonProperty("partner")
    private String partner;

    /**
     * Status.
     */
    @Schema(
            name = "status",
            description = "Geographical zone status",
            required = true,
            example = "AVAILABLE")
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
    public PartnerZone(UUID mspId) {
        this(mspId, null, ZoneStatus.NO_ZONE);
    }

    /**
     * Constructor.
     *
     * @param partnerId   MSP identifier.
     * @param partnerName MSP name.
     * @param status      Status of the zone.
     */
    public PartnerZone(UUID partnerId, String partnerName, ZoneStatus status) {
        this.partnerId = partnerId;
        this.partner = partnerName;
        this.status = status;
    }

}

