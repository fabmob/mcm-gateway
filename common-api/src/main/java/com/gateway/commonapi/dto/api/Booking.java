package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Booking
 */
@Data
@Validated
@Getter
@Setter
public class Booking implements Serializable {


    @Schema(required = true, description = "Booking id is common between both operators, and must be created as a [UUID](https://datatracker.ietf.org/doc/html/rfc4122) by whoever initiates the booking.  Usage of a [4 UUID](https://datatracker.ietf.org/doc/html/rfc4122#section-4.4) generation algorithm is advised.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    @Valid
    @JsonProperty("id")
    private UUID id = null;

    @Schema(required = true, description = "")
    @NotNull
    @Valid
    @JsonProperty("driver")
    private User driver = null;

    @Schema(required = true, description = "")
    @NotNull
    @Valid
    @JsonProperty("passenger")
    private User passenger = null;

    @Schema(required = true, description = "Passenger pickup datetime as a UNIX UTC timestamp in seconds.")
    @NotNull
    @Valid
    @JsonProperty("passengerPickupDate")
    private BigDecimal passengerPickupDate = null;

    @Schema(required = true, description = "Latitude of the passenger pick-up point.", example = "2.293444")
    @NotNull
    @JsonProperty("passengerPickupLat")
    private Double passengerPickupLat = null;

    @Schema(required = true, description = "Longitude of the passenger pick-up point.", example = "48.858997")
    @NotNull
    @JsonProperty("passengerPickupLng")
    private Double passengerPickupLng = null;

    @Schema(required = true, description = "Latitude of the passenger drop-off point.", example = "1.443332")
    @NotNull
    @JsonProperty("passengerDropLat")
    private Double passengerDropLat = null;

    @Schema(required = true, description = "Longitude of the passenger drop-off point.", example = "43.604583")
    @NotNull
    @JsonProperty("passengerDropLng")
    private Double passengerDropLng = null;

    @Schema(description = "String representing the pickup-up address.", example = "Pont d'Iéna, Paris")
    @JsonProperty("passengerPickupAddress")
    private String passengerPickupAddress = null;

    @Schema(description = "String representing the drop-off address.", example = "Place du Capitole, Toulouse")
    @JsonProperty("passengerDropAddress")
    private String passengerDropAddress = null;

    @Schema(required = true, description = "", example = "WAITING_CONFIRMATION")
    @NotNull
    @Valid
    @JsonProperty("status")
    private BookingStatus status = null;

    @Schema(description = "Carpooling duration in seconds.", example = "700000")
    @JsonProperty("duration")
    private Integer duration = null;

    @Schema(description = "Carpooling distance in meters. ", example = "700000")
    @JsonProperty("distance")
    private Integer distance = null;

    @Schema(description = "URL of the booking on the webservice provider platform.", example = "carpool.mycity.com/booking/booking00321")
    @JsonProperty("webUrl")
    private String webUrl = null;

    @Schema(required = true, description = "")
    @NotNull
    @Valid
    @JsonProperty("price")
    private Price price = null;

    @Schema(description = "")
    @Valid
    @JsonProperty("car")
    private Car car = null;

    @Schema(description = "ID of the Driver's journey to which the booking is related (if any). Unique given the `User`'s `operator` property.", example = "Journey00001")
    @Size(min = 1, max = 255)
    @JsonProperty("driverJourneyId")
    private String driverJourneyId = null;

    @Schema(description = "ID of the Passenger's journey to which the booking is related (if any). Unique given the `User`'s `operator` property.", example = "Journey00435")
    @Size(min = 1, max = 255)
    @JsonProperty("passengerJourneyId")
    private String passengerJourneyId = null;


    public Booking id(UUID id) {
        this.id = id;
        return this;
    }

    public Booking driver(User driver) {
        this.driver = driver;
        return this;
    }

    public Booking passenger(User passenger) {
        this.passenger = passenger;
        return this;
    }

    public Booking passengerPickupDate(BigDecimal passengerPickupDate) {
        this.passengerPickupDate = passengerPickupDate;
        return this;
    }

    public Booking passengerPickupLat(Double passengerPickupLat) {
        this.passengerPickupLat = passengerPickupLat;
        return this;
    }

    public Booking passengerPickupLng(Double passengerPickupLng) {
        this.passengerPickupLng = passengerPickupLng;
        return this;
    }

    public Booking passengerDropLat(Double passengerDropLat) {
        this.passengerDropLat = passengerDropLat;
        return this;
    }

    public Booking passengerDropLng(Double passengerDropLng) {
        this.passengerDropLng = passengerDropLng;
        return this;
    }

    public Booking passengerPickupAddress(String passengerPickupAddress) {
        this.passengerPickupAddress = passengerPickupAddress;
        return this;
    }

    public Booking passengerDropAddress(String passengerDropAddress) {
        this.passengerDropAddress = passengerDropAddress;
        return this;
    }

    public Booking status(BookingStatus status) {
        this.status = status;
        return this;
    }

    public Booking duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public Booking distance(Integer distance) {
        this.distance = distance;
        return this;
    }

    public Booking webUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public Booking price(Price price) {
        this.price = price;
        return this;
    }

    public Booking car(Car car) {
        this.car = car;
        return this;
    }

    public Booking driverJourneyId(String driverJourneyId) {
        this.driverJourneyId = driverJourneyId;
        return this;
    }

    public Booking passengerJourneyId(String passengerJourneyId) {
        this.passengerJourneyId = passengerJourneyId;
        return this;
    }

}
