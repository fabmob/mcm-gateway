package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.Day;
import com.gateway.commonapi.utils.enums.UserTypeEnum;
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

/**
 * TOMP-API
 * SystemHours
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SystemHours implements Serializable {

  @JsonProperty("userType")
  private UserTypeEnum userType = null;

  @JsonProperty("stationId")
  private String stationId = null;

  @JsonProperty("regionId")
  private String regionId = null;

  @JsonProperty("startTime")
  private String startTime = null;

  @JsonProperty("endTime")
  private String endTime = null;

  @JsonProperty("days")
  @Valid
  private List<Day> days = new ArrayList<>();

  public SystemHours userType(UserTypeEnum userType) {
    this.userType = userType;
    return this;
  }

  /**
   * This indicates that this set of rental hours applies to either members or non-members only.
   * @return userType
   **/
  @Schema(example = "MEMBER", description = "This indicates that this set of rental hours applies to either members or non-members only.")
  
    public UserTypeEnum getUserType() {
    return userType;
  }

  public void setUserType(UserTypeEnum userType) {
    this.userType = userType;
  }

  public SystemHours stationId(String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * If this parameter is present, it means that startTime and endTime correspond to the opening and closing hours of the station. (GET /operator/stations)
   * @return stationId
   **/
  @Schema(description = "If this parameter is present, it means that startTime and endTime correspond to the opening and closing hours of the station. (GET /operator/stations)")
  
    public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public SystemHours regionId(String regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * If this parameter is present, it means that startTime and endTime correspond to the opening and closing hours for the region. (GET /operator/regions)
   * @return regionId
   **/
  @Schema(description = "If this parameter is present, it means that startTime and endTime correspond to the opening and closing hours for the region. (GET /operator/regions)")
  
    public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public SystemHours startTime(String startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public SystemHours endTime(String endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * Get endTime
   * @return endTime
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public SystemHours days(List<Day> days) {
    this.days = days;
    return this;
  }

  public SystemHours addDaysItem(Day daysItem) {
    this.days.add(daysItem);
    return this;
  }

  /**
   * An array of abbreviations (first 3 letters) of English names of the days of the week that this hour object applies to (i.e. [\"mon\", \"tue\"]). Each day can only appear once within all of the hours objects in this feed.
   * @return days
   **/
  @Schema(required = true, description = "An array of abbreviations (first 3 letters) of English names of the days of the week that this hour object applies to (i.e. [\"mon\", \"tue\"]). Each day can only appear once within all of the hours objects in this feed.")
      @NotNull
    @Valid
    public List<Day> getDays() {
    return days;
  }

  public void setDays(List<Day> days) {
    this.days = days;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemHours {\n");
    
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    days: ").append(toIndentedString(days)).append("\n");
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
}
