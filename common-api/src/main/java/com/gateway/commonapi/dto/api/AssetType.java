package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.AssetClass;
import com.gateway.commonapi.utils.enums.MSPType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AssetType
 * TOMP-API Standard
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about an asset type")
@JsonPropertyOrder({ "assetTypeId", "partnerId","type","stationId","nrAvailable","assets","assetClass","assetSubClass","sharedProperties","applicablePricings","conditions"})
public class AssetType implements Serializable {

  @Schema(
          name="assetTypeId",
          description="Unique identifier of an asset type",
          required = true)
  @NotNull
  @JsonProperty("assetTypeId")
  private String assetTypeId = null;

  @JsonProperty("partnerId")
  @NotNull
  @Schema(description = "partner Identifier",
          example = "b814c97e-df56-4651-ac50-11525537964f")
  private UUID partnerId;

  @JsonProperty("type")
  @NotNull
  @Schema(description = "assets type")
  private MSPType type;

  @Schema(
          name="stationId",
          example="'XX:Y:12345678",
          description="Identifier of the station")
  @JsonProperty("stationId")
  private String stationId = null;

  @Schema(
          name = "nrAvailable",
          description = "Number of available assets in the station")
  @JsonProperty("nrAvailable")
  private Integer nrAvailable = null;

  @Schema(
          name = "assets",
          description = "List of assets")
  @JsonProperty("assets")
  @Valid
  private List<Asset> assets = null;

  @Schema(
          name = "assetClass",
          description = "Asset class (\"CAR\",\"SELFDRIVE\"...)")
  @JsonProperty("assetClass")
  private AssetClass assetClass = null;

  @Schema(
          name = "assetSubClass",
          description = "a more precise classification of the asset, like 'cargo bike', 'public bus', 'coach bus', 'office bus', 'water taxi', 'segway'. This is mandatory when using 'OTHER' as class")
  @JsonProperty("assetSubClass")
  private String assetSubClass = null;

  @Schema(
          name = "sharedProperties",
          description = "properties shared by assets in the station")
  @JsonProperty("sharedProperties")
  private AssetProperties sharedProperties = null;

  @Schema(
          name = "applicablePricings",
          description = "List of pricing plans that can be applicable for this assetType. Business logic to determine the final pricing plan is not exposed.")
  @JsonProperty("applicablePricings")
  @Valid
  private List<SystemPricingPlan> applicablePricings = null;


  @Schema(
          name = "name",
          description = "List of extra information about the asset type, making it possible to f.x. specifying that booking this car requires a driver license.")
  @JsonProperty("conditions")
  @Valid
  private List<OneOfassetTypeConditionsItems> conditions = null;


  /**
   * Unique identifier of an asset type,
   * @return id
   **/
  @Schema(required = true, description = "Unique identifier of an asset type,")
      @NotNull

    public String getAssetTypeId() {
    return assetTypeId;
  }

  public void setAssetTypeId(String id) {
    this.assetTypeId = id;
  }

  public AssetType stationId(String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * If stationId is present, the nrAvailable is expected to find the availability at that particular station
   * @return stationId
   **/
  @Schema(description = "If stationId is present, the nrAvailable is expected to find the availability at that particular station")
  
    public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public AssetType nrAvailable(Integer nrAvailable) {
    this.nrAvailable = nrAvailable;
    return this;
  }

  /**
   * Get nrAvailable
   * @return nrAvailable
   **/
  @Schema(description = "Number of available assets in the station")
  
    public Integer getNrAvailable() {
    return nrAvailable;
  }

  public void setNrAvailable(Integer nrAvailable) {
    this.nrAvailable = nrAvailable;
  }

  public AssetType assets(List<Asset> assets) {
    this.assets = assets;
    return this;
  }

  public AssetType addAssetsItem(Asset assetsItem) {
    if (this.assets == null) {
      this.assets = new ArrayList<>();
    }
    this.assets.add(assetsItem);
    return this;
  }

  /**
   * Get assets
   * @return assets
   **/
  @Schema(description = "List of assets")
      @Valid
    public List<Asset> getAssets() {
    return assets;
  }

  public void setAssets(List<Asset> assets) {
    this.assets = assets;
  }

  public AssetType assetClass(AssetClass assetClass) {
    this.assetClass = assetClass;
    return this;
  }

  /**
   * Get assetClass
   * @return assetClass
   **/
  @Schema(description = "")
  
    @Valid
    public AssetClass getAssetClass() {
    return assetClass;
  }

  public void setAssetClass(AssetClass assetClass) {
    this.assetClass = assetClass;
  }

  public AssetType assetSubClass(String assetSubClass) {
    this.assetSubClass = assetSubClass;
    return this;
  }

  /**
   * a more precise classification of the asset, like 'cargo bike', 'public bus', 'coach bus', 'office bus', 'water taxi',  'segway'. This is mandatory when using 'OTHER' as class.
   * @return assetSubClass
   **/
  @Schema(description = "a more precise classification of the asset, like 'cargo bike', 'public bus', 'coach bus', 'office bus', 'water taxi',  'segway'. This is mandatory when using 'OTHER' as class.")
  
    public String getAssetSubClass() {
    return assetSubClass;
  }

  public void setAssetSubClass(String assetSubClass) {
    this.assetSubClass = assetSubClass;
  }

  public AssetType sharedProperties(AssetProperties sharedProperties) {
    this.sharedProperties = sharedProperties;
    return this;
  }

  /**
   * Get sharedProperties
   * @return sharedProperties
   **/
  @Schema(description = "")
  
    @Valid
    public AssetProperties getSharedProperties() {
    return sharedProperties;
  }

  public void setSharedProperties(AssetProperties sharedProperties) {
    this.sharedProperties = sharedProperties;
  }

  public AssetType applicablePricings(List<SystemPricingPlan> applicablePricings) {
    this.applicablePricings = applicablePricings;
    return this;
  }

  public AssetType addApplicablePricingsItem(SystemPricingPlan applicablePricingsItem) {
    if (this.applicablePricings == null) {
      this.applicablePricings = new ArrayList<>();
    }
    this.applicablePricings.add(applicablePricingsItem);
    return this;
  }

  /**
   * pricing plans that can be applicable for this assetType. Business logic to determine the final pricing plan is not exposed. Just call the plannings endpoint (v1.2) or the inquiries endpoint (v.1.3)
   * @return applicablePricings
   **/
  @Schema(description = "pricing plans that can be applicable for this assetType. Business logic to determine the final pricing plan is not exposed. Just call the plannings endpoint (v1.2) or the inquiries endpoint (v.1.3)")
      @Valid
    public List<SystemPricingPlan> getApplicablePricings() {
    return applicablePricings;
  }

  public void setApplicablePricings(List<SystemPricingPlan> applicablePricings) {
    this.applicablePricings = applicablePricings;
  }

  public AssetType conditions(List<OneOfassetTypeConditionsItems> conditions) {
    this.conditions = conditions;
    return this;
  }

  public AssetType addConditionsItem(OneOfassetTypeConditionsItems conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * extra information about the asset type, making it possible to f.x. specifying that booking this car requires a driver license.
   * @return conditions
   **/
  @Schema(description = "extra information about the asset type, making it possible to f.x. specifying that booking this car requires a driver license.")
  
    public List<OneOfassetTypeConditionsItems> getConditions() {
    return conditions;
  }

  public void setConditions(List<OneOfassetTypeConditionsItems> conditions) {
    this.conditions = conditions;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetType {\n");
    
    sb.append("    assetTypeId: ").append(toIndentedString(assetTypeId)).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    nrAvailable: ").append(toIndentedString(nrAvailable)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    assetClass: ").append(toIndentedString(assetClass)).append("\n");
    sb.append("    assetSubClass: ").append(toIndentedString(assetSubClass)).append("\n");
    sb.append("    sharedProperties: ").append(toIndentedString(sharedProperties)).append("\n");
    sb.append("    applicablePricings: ").append(toIndentedString(applicablePricings)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public AssetType(String assetTypeId, UUID partnerId, String stationId, Integer nrAvailable, List<Asset> assets, AssetClass assetClass) {
    this.assetTypeId = assetTypeId;
    this.partnerId = partnerId;
    this.stationId = stationId;
    this.nrAvailable = nrAvailable;
    this.assets = assets;
    this.assetClass = assetClass;
  }
}
