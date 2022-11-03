package com.gateway.api.model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.api.model.PartnerMeta;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.PriceListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;


/**
 * Bean for details about an potential action.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Generic response")
public class MockResponse implements Serializable {

    @Schema(
            name = "partnerMeta",
            required = false)
    @JsonProperty("partnerMeta")
    private List<PartnerMeta> partnerMeta;

    @Schema(
            name = "assets",
            required = false)
    @JsonProperty("assets")
    private List<Asset> assets;

    @Schema(
            name = "availableAsset",
            required = false)
    @JsonProperty("availableAsset")
    private  List<AssetType> availableAsset;

    @Schema(
            name = "stations",
            required = false)
    @JsonProperty("stations")
    private  List<Station> stations;

    @Schema(
            name = "stationsStatus",
            required = false)
    @JsonProperty("stationsStatus")
    private List<StationStatus> stationsStatus;

    @Schema(
            name = "priceList",
            required = false)
    @JsonProperty("priceList")
    private List<List<PriceListDTO>>  priceList;

    @Schema(
            name = "vehicleTypes",
            required = false)
    @JsonProperty("vehicleTypes")
    private List<VehicleTypes> vehicleTypes;

    @Schema(
            name = "zones",
            required = false)
    @JsonProperty("zones")
    private List <PartnerZone> zones;

}
