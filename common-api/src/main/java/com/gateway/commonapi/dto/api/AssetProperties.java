package com.gateway.commonapi.dto.api;


import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Aspects of an asset or assetType. Most aspects are optional and should only be used when applicable.
 */
@Schema(description = "Aspects of an asset or assetType. Most aspects are optional and should only be used when applicable.")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetProperties implements Serializable {

    @Schema(description = "Human readable name of the place",
            example = "Place de la République")
    @JsonProperty("name")
    private String name = null;

    @Schema(description = "Taxi operator", example = "G7")
    @JsonProperty("operator")
    private String operator = null;

    @JsonProperty("location")
    private Place location = null;



    @Schema( example = "NONE")
    @JsonProperty("fuel")
    private FuelEnum fuel = null;


    @Schema(example = "A")
    @JsonProperty("energyLabel")
    private EnergyLabelEnum energyLabel = null;

    @Schema(example = "0")
    @JsonProperty("co2PerKm")
    private Float co2PerKm = null;

    @Schema(example = "6543.0")
    @JsonProperty("currentRangeMeters")
    private Float currentRangeMeters;

    @Schema(example = "Bikebrand")
    @JsonProperty("brand")
    private String brand = null;


    @Schema(example = "Bikemodel")
    @JsonProperty("model")
    private String model = null;


    @Schema(example = "2020")
    @JsonProperty("buildingYear")
    private Integer buildingYear = null;


    @Schema(example = "false", description = "true indicates asset is allowed to travel abroad")
    @JsonProperty("travelAbroad")
    private Boolean travelAbroad = null;


    @Schema(example = "false", description = "true indicates airconditioning required")
    @JsonProperty("airConditioning")
    private Boolean airConditioning = null;

    @Schema(example = "false", description = "true indicates cabrio required")
    @JsonProperty("cabrio")
    private Boolean cabrio = null;

    @Schema(example = "red")
    @JsonProperty("colour")
    private String colour = null;

    @JsonProperty("cargo")
    @Schema(description = "describes options to carry cargo",
            example = "small luggage")
    private String cargo = null;


    @Schema(description = "describes if asset is or needs to be easily accessible",
            example = "LIFT")
    @JsonProperty("easyAccessibility")
    private EasyAccessibilityEnum easyAccessibility = null;

    @Schema(
            example = "7")
    @JsonProperty("gears")
    private Integer gears = null;


    @Schema(
            example = "MANUEL")
    @JsonProperty("gearbox")
    private GearboxEnum gearbox = null;

    @Schema(
            example = "Bike1111.jpg")
    @JsonProperty("image")
    private String image = null;

    @Schema(
            example = "false")
    @JsonProperty("infantSeat")
    private Boolean infantSeat = null;

    @Schema(
            example = "1")
    @JsonProperty("persons")
    private Integer persons = null;

    @Schema(
            example = "false")
    @JsonProperty("pets")
    private Boolean pets = null;


    @Schema(
            example = "MUSCLE")
    @JsonProperty("propulsion")
    private PropulsionEnum propulsion = null;

    @Schema(
            example = "true")
    @JsonProperty("smoking")
    private Boolean smoking = null;

    @Schema( description = "percentage of charge available",
            example = "50")
    @JsonProperty("stateOfCharge")
    private Integer stateOfCharge = null;

    @Schema( description = "true indicates towing hook required",
            example = "false")
    @JsonProperty("towingHook")
    private Boolean towingHook = null;

    @Schema(
            example = "false")
    @JsonProperty("undergroundParking")
    private Boolean undergroundParking = null;

    @Schema(
            example = "false")
    @JsonProperty("winterTires")
    private Boolean winterTires = null;

    @Schema(
            example = "false")
    @JsonProperty("snowChains")
    private Boolean snowChains = null;


    @Schema(
            example = "50")
    @JsonProperty("maxSpeed")
    private Integer maxSpeed = null;

    @Schema(
            example = "true")
    @JsonProperty("helmetRequired")
    private Boolean helmetRequired = false;

    @Schema(
            example = "with confortable seat")
    @JsonProperty("other")
    private String other = null;

    @Schema(
            example = "0")
    @JsonProperty("nrOfDoors")
    private Integer nrOfDoors = null;

    @JsonProperty("meta")
    @Valid
    private Map<String, Object> meta = null;

    @Schema(
            example = "QR")
    @JsonProperty("accessMethods")
    @Valid
    private List<AssetAccessMethods> accessMethods = null;

    @JsonProperty("ancillaries")
    @Valid
    private List<Requirement> ancillaries = null;



    public AssetProperties name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of asset (type), required in either assetType or asset, should match Content-Language
     * @return name
     **/
    @Schema(description = "name of asset (type), required in either assetType or asset, should match Content-Language")

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetProperties location(Place location) {
        this.location = location;
        return this;
    }

    /**
     * Get location
     * @return location
     **/
    @Schema(description = "")

    @Valid
    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

    public AssetProperties fuel(FuelEnum fuel) {
        this.fuel = fuel;
        return this;
    }

    /**
     * Get fuel
     * @return fuel
     **/
    @Schema(description = "")

    public FuelEnum getFuel() {
        return fuel;
    }

    public void setFuel(FuelEnum fuel) {
        this.fuel = fuel;
    }

    public AssetProperties energyLabel(EnergyLabelEnum energyLabel) {
        this.energyLabel = energyLabel;
        return this;
    }

    /**
     * Energy efficiency
     * @return energyLabel
     **/
    @Schema(description = "Energy efficiency")

    public EnergyLabelEnum getEnergyLabel() {
        return energyLabel;
    }

    public void setEnergyLabel(EnergyLabelEnum energyLabel) {
        this.energyLabel = energyLabel;
    }

    public AssetProperties co2PerKm(Float co2PerKm) {
        this.co2PerKm = co2PerKm;
        return this;
    }

    /**
     * Get co2PerKm
     * minimum: 0
     * @return co2PerKm
     **/
    @Schema(description = "")

    @DecimalMin("0")  public Float getCo2PerKm() {
        return co2PerKm;
    }

    public void setCo2PerKm(Float co2PerKm) {
        this.co2PerKm = co2PerKm;
    }

    public AssetProperties brand(String brand) {
        this.brand = brand;
        return this;
    }

    /**
     * brand of the asset
     * @return brand
     **/
    @Schema(description = "brand of the asset")

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AssetProperties model(String model) {
        this.model = model;
        return this;
    }

    /**
     * Get model
     * @return model
     **/
    @Schema(description = "")

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public AssetProperties buildingYear(Integer buildingYear) {
        this.buildingYear = buildingYear;
        return this;
    }

    /**
     * Get buildingYear
     * @return buildingYear
     **/
    @Schema(description = "")

    public Integer getBuildingYear() {
        return buildingYear;
    }

    public void setBuildingYear(Integer buildingYear) {
        this.buildingYear = buildingYear;
    }

    public AssetProperties travelAbroad(Boolean travelAbroad) {
        this.travelAbroad = travelAbroad;
        return this;
    }

    /**
     * true indicates asset is allowed to travel abroad
     * @return travelAbroad
     **/
    @Schema(description = "true indicates asset is allowed to travel abroad")

    public Boolean isTravelAbroad() {
        return travelAbroad;
    }

    public void setTravelAbroad(Boolean travelAbroad) {
        this.travelAbroad = travelAbroad;
    }

    public AssetProperties airConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
        return this;
    }

    /**
     * true indicates airconditioning required
     * @return airConditioning
     **/
    @Schema(description = "true indicates airconditioning required")

    public Boolean isAirConditioning() {
        return airConditioning;
    }

    public void setAirConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public AssetProperties cabrio(Boolean cabrio) {
        this.cabrio = cabrio;
        return this;
    }

    /**
     * true indicates cabrio required
     * @return cabrio
     **/
    @Schema(description = "true indicates cabrio required")

    public Boolean isCabrio() {
        return cabrio;
    }

    public void setCabrio(Boolean cabrio) {
        this.cabrio = cabrio;
    }

    public AssetProperties colour(String colour) {
        this.colour = colour;
        return this;
    }

    /**
     * colour of the asset, should match Content-Language
     * @return colour
     **/
    @Schema(description = "colour of the asset, should match Content-Language")

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public AssetProperties cargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    /**
     * describes options to carry cargo, should match Content-Language
     * @return cargo
     **/
    @Schema(description = "describes options to carry cargo, should match Content-Language")

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public AssetProperties easyAccessibility(EasyAccessibilityEnum easyAccessibility) {
        this.easyAccessibility = easyAccessibility;
        return this;
    }

    /**
     * describes if asset is or needs to be easily accessible
     * @return easyAccessibility
     **/
    @Schema(description = "describes if asset is or needs to be easily accessible")

    public EasyAccessibilityEnum getEasyAccessibility() {
        return easyAccessibility;
    }

    public void setEasyAccessibility(EasyAccessibilityEnum easyAccessibility) {
        this.easyAccessibility = easyAccessibility;
    }

    public AssetProperties gears(Integer gears) {
        this.gears = gears;
        return this;
    }

    /**
     * number of gears of the asset
     * @return gears
     **/
    @Schema(description = "number of gears of the asset")

    public Integer getGears() {
        return gears;
    }

    public void setGears(Integer gears) {
        this.gears = gears;
    }

    public AssetProperties gearbox(GearboxEnum gearbox) {
        this.gearbox = gearbox;
        return this;
    }

    /**
     * type of gearbox
     * @return gearbox
     **/
    @Schema(description = "type of gearbox")

    public GearboxEnum getGearbox() {
        return gearbox;
    }

    public void setGearbox(GearboxEnum gearbox) {
        this.gearbox = gearbox;
    }

    public AssetProperties image(String image) {
        this.image = image;
        return this;
    }

    /**
     * Link to an image of the asset
     * @return image
     **/
    @Schema(example = "https://files.fietsersbond.nl/app/uploads/2014/10/30151126/ST2_Men_Side_CityKit-Stromer.jpg", description = "Link to an image of the asset")

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public AssetProperties infantSeat(Boolean infantSeat) {
        this.infantSeat = infantSeat;
        return this;
    }

    /**
     * true indicates infant seat required
     * @return infantSeat
     **/
    @Schema(description = "true indicates infant seat required")

    public Boolean isInfantSeat() {
        return infantSeat;
    }

    public void setInfantSeat(Boolean infantSeat) {
        this.infantSeat = infantSeat;
    }

    public AssetProperties persons(Integer persons) {
        this.persons = persons;
        return this;
    }

    /**
     * number of persons able to use the asset
     * minimum: 1
     * @return persons
     **/
    @Schema(description = "number of persons able to use the asset")

    @Min(1)  public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public AssetProperties pets(Boolean pets) {
        this.pets = pets;
        return this;
    }

    /**
     * true indicates pets are allowed on asset
     * @return pets
     **/
    @Schema(description = "true indicates pets are allowed on asset")

    public Boolean isPets() {
        return pets;
    }

    public void setPets(Boolean pets) {
        this.pets = pets;
    }

    public AssetProperties propulsion(PropulsionEnum propulsion) {
        this.propulsion = propulsion;
        return this;
    }

    /**
     * way in which the asset is powered
     * @return propulsion
     **/
    @Schema(description = "way in which the asset is powered")

    public PropulsionEnum getPropulsion() {
        return propulsion;
    }

    public void setPropulsion(PropulsionEnum propulsion) {
        this.propulsion = propulsion;
    }

    public AssetProperties smoking(Boolean smoking) {
        this.smoking = smoking;
        return this;
    }

    /**
     * true indicates smoking is allowed on asset
     * @return smoking
     **/
    @Schema(description = "true indicates smoking is allowed on asset")

    public Boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    public AssetProperties stateOfCharge(Integer stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
        return this;
    }

    /**
     * percentage of charge available
     * minimum: 0
     * maximum: 100
     * @return stateOfCharge
     **/
    @Schema(description = "percentage of charge available")

    @Min(0) @Max(100)   public Integer getStateOfCharge() {
        return stateOfCharge;
    }

    public void setStateOfCharge(Integer stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    public AssetProperties towingHook(Boolean towingHook) {
        this.towingHook = towingHook;
        return this;
    }

    /**
     * true indicates towing hook required
     * @return towingHook
     **/
    @Schema(description = "true indicates towing hook required")

    public Boolean isTowingHook() {
        return towingHook;
    }

    public void setTowingHook(Boolean towingHook) {
        this.towingHook = towingHook;
    }

    public AssetProperties undergroundParking(Boolean undergroundParking) {
        this.undergroundParking = undergroundParking;
        return this;
    }

    /**
     * true indicates underground parking is allowed with asset
     * @return undergroundParking
     **/
    @Schema(description = "true indicates underground parking is allowed with asset")

    public Boolean isUndergroundParking() {
        return undergroundParking;
    }

    public void setUndergroundParking(Boolean undergroundParking) {
        this.undergroundParking = undergroundParking;
    }

    public AssetProperties winterTires(Boolean winterTires) {
        this.winterTires = winterTires;
        return this;
    }

    /**
     * true indicates winter tires required
     * @return winterTires
     **/
    @Schema(description = "true indicates winter tires required")

    public Boolean isWinterTires() {
        return winterTires;
    }

    public void setWinterTires(Boolean winterTires) {
        this.winterTires = winterTires;
    }

    public AssetProperties maxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    /**
     * the maximum allowed speed for this asset (in km/h)
     * @return maxSpeed
     **/
    @Schema(description = "the maximum allowed speed for this asset (in km/h)")

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public AssetProperties helmetRequired(Boolean helmetRequired) {
        this.helmetRequired = helmetRequired;
        return this;
    }

    /**
     * is a helmet required to operate this asset
     * @return helmetRequired
     **/
    @Schema(description = "is a helmet required to operate this asset")

    public Boolean isHelmetRequired() {
        return helmetRequired;
    }

    public void setHelmetRequired(Boolean helmetRequired) {
        this.helmetRequired = helmetRequired;
    }

    public AssetProperties other(String other) {
        this.other = other;
        return this;
    }

    /**
     * free text to describe asset, should match Content-Language
     * @return other
     **/
    @Schema(description = "free text to describe asset, should match Content-Language")

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public AssetProperties nrOfDoors(Integer nrOfDoors) {
        this.nrOfDoors = nrOfDoors;
        return this;
    }

    /**
     * the number of doors of the vehicle. Return only when applicable
     * @return nrOfDoors
     **/
    @Schema(description = "the number of doors of the vehicle. Return only when applicable")

    public Integer getNrOfDoors() {
        return nrOfDoors;
    }

    public void setNrOfDoors(Integer nrOfDoors) {
        this.nrOfDoors = nrOfDoors;
    }

    public AssetProperties meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public AssetProperties putMetaItem(String key, Object metaItem) {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.put(key, metaItem);
        return this;
    }

    /**
     * this object can contain extra information about the type of asset. For instance values from the 'Woordenboek Reizigerskenmerken'. [https://github.com/efel85/TOMP-API/issues/17]. These values can also be used in the planning.
     * @return meta
     **/
    @Schema(description = "this object can contain extra information about the type of asset. For instance values from the 'Woordenboek Reizigerskenmerken'. [https://github.com/efel85/TOMP-API/issues/17]. These values can also be used in the planning.")

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public AssetProperties accessMethods(List<AssetAccessMethods> accessMethods) {
        this.accessMethods = accessMethods;
        return this;
    }

    public AssetProperties addAccessMethodsItem(AssetAccessMethods accessMethodsItem) {
        if (this.accessMethods == null) {
            this.accessMethods = new ArrayList<>();
        }
        this.accessMethods.add(accessMethodsItem);
        return this;
    }

    /**
     * access method for trip execution. Data will be delivered in the response of /booking/{id}/events - COMMIT or /leg/{id}/events - PREPARE (preferred).
     * @return accessMethods
     **/
    @Schema(description = "access method for trip execution. Data will be delivered in the response of /booking/{id}/events - COMMIT or /leg/{id}/events - PREPARE (preferred).")
    @Valid
    public List<AssetAccessMethods> getAccessMethods() {
        return accessMethods;
    }

    public void setAccessMethods(List<AssetAccessMethods> accessMethods) {
        this.accessMethods = accessMethods;
    }

    public AssetProperties ancillaries(List<Requirement> ancillaries) {
        this.ancillaries = ancillaries;
        return this;
    }

    public AssetProperties addAncillariesItem(Requirement ancillariesItem) {
        if (this.ancillaries == null) {
            this.ancillaries = new ArrayList<>();
        }
        this.ancillaries.add(ancillariesItem);
        return this;
    }

    /**
     * Get ancillaries
     * @return ancillaries
     **/
    @Schema(description = "")
    @Valid
    public List<Requirement> getAncillaries() {
        return ancillaries;
    }

    public void setAncillaries(List<Requirement> ancillaries) {
        this.ancillaries = ancillaries;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AssetProperties {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    fuel: ").append(toIndentedString(fuel)).append("\n");
        sb.append("    energyLabel: ").append(toIndentedString(energyLabel)).append("\n");
        sb.append("    co2PerKm: ").append(toIndentedString(co2PerKm)).append("\n");
        sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
        sb.append("    model: ").append(toIndentedString(model)).append("\n");
        sb.append("    buildingYear: ").append(toIndentedString(buildingYear)).append("\n");
        sb.append("    travelAbroad: ").append(toIndentedString(travelAbroad)).append("\n");
        sb.append("    airConditioning: ").append(toIndentedString(airConditioning)).append("\n");
        sb.append("    cabrio: ").append(toIndentedString(cabrio)).append("\n");
        sb.append("    colour: ").append(toIndentedString(colour)).append("\n");
        sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
        sb.append("    easyAccessibility: ").append(toIndentedString(easyAccessibility)).append("\n");
        sb.append("    gears: ").append(toIndentedString(gears)).append("\n");
        sb.append("    gearbox: ").append(toIndentedString(gearbox)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    infantSeat: ").append(toIndentedString(infantSeat)).append("\n");
        sb.append("    persons: ").append(toIndentedString(persons)).append("\n");
        sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
        sb.append("    propulsion: ").append(toIndentedString(propulsion)).append("\n");
        sb.append("    smoking: ").append(toIndentedString(smoking)).append("\n");
        sb.append("    stateOfCharge: ").append(toIndentedString(stateOfCharge)).append("\n");
        sb.append("    towingHook: ").append(toIndentedString(towingHook)).append("\n");
        sb.append("    undergroundParking: ").append(toIndentedString(undergroundParking)).append("\n");
        sb.append("    winterTires: ").append(toIndentedString(winterTires)).append("\n");
        sb.append("    maxSpeed: ").append(toIndentedString(maxSpeed)).append("\n");
        sb.append("    helmetRequired: ").append(toIndentedString(helmetRequired)).append("\n");
        sb.append("    other: ").append(toIndentedString(other)).append("\n");
        sb.append("    nrOfDoors: ").append(toIndentedString(nrOfDoors)).append("\n");
        sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
        sb.append("    accessMethods: ").append(toIndentedString(accessMethods)).append("\n");
        sb.append("    ancillaries: ").append(toIndentedString(ancillaries)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
