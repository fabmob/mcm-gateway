package com.gateway.api.service.ivservice;

import com.gateway.api.model.PriceList;
import com.gateway.commonapi.dto.api.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


public interface IVService {

    /**
     * Retrieve a list of stations for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @param lon
     * @param lat
     * @param rad
     * @return List of stations (static informations) for the MSP
     */
    List<Station> getStations(UUID mspId, Float lon, Float lat, Float rad);


    /**
     * Retrieve a list of stations status for a MSP.
     *
     * @param mspId     Identifier of the MSP.
     * @param stationId
     * @return List of stations (dynamic informations) for the MSP
     */
    List<StationStatus> getStationStatus(UUID mspId, String stationId);

    /**
     * Retrieve a list of assets for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of assets for the MSP.
     */
    List<Asset> getAssets(UUID mspId);


    /**
     * Retrieve a list of available assets for a MSP.
     *
     * @param mspId     Identifier of the MSP.
     * @param stationId
     * @param lon
     * @param lat
     * @param rad
     * @return List of available assets for the MSP.
     */
    List<AssetType> getAvailableAssets(UUID mspId, String stationId, Float lon, Float lat, Float rad);


    /**
     * Retrieve a global view.
     *
     * @return Global view.
     */
    GlobalView getGlobalView() throws IOException, InterruptedException;


    /**
     * Retrieve a global view for around me.
     *
     * @param mspAroundMeRequest
     * @return Global view.
     */
    GlobalView getAroundMe(MSPAroundMeRequest mspAroundMeRequest);

    /**
     * Retrieve all vehicle types of an MSP
     *
     * @param mspId
     * @return
     */
    List<VehicleTypes> getVehicleTypes(UUID mspId);

    /**
     * Retrieve all pricing plans of an MSP
     *
     * @param mspId
     * @return
     */
    List<PriceList> getPricingPlan(UUID mspId, String stationId);

    /**
     *  Retrieve all Passenger Regular Trips
     * @param departureLat Latitude of searched departure point.
     * @param departureLng Longitude of searched departure point.
     * @param arrivalLat Latitude of searched arrival point.
     * @param arrivalLng Longitude of searched arrival point.
     * @param departureTimeOfDay Departure time of day represented as RFC3339 partial-time.
     * @param departureWeekdays Departure days of week. The retrieved trips should have at least one schedule applicable on one of the departureWeekdays.
     * @param timeDelta Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius Search radius in kilometers around the arrival point.
     * @param minDepartureDate Minimum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param maxDepartureDate Maximum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param count Maximum number of returned results. If missing, all matching results are returned.
     * @return list of Passenger Regular Trips
     */
    List<PassengerRegularTrip> getPassengerRegularTrips( UUID mspId,Float departureLat, Float departureLng, Float arrivalLat,
                                                         Float arrivalLng, String departureTimeOfDay, List<String> departureWeekdays, Integer timeDelta,
                                                         Float departureRadius, Float arrivalRadius, Integer minDepartureDate, Integer maxDepartureDate, Integer count);


    /**
     * Retrieve all Driver Regular Trips
     * @param departureLat Latitude of searched departure point.
     * @param departureLng Longitude of searched departure point.
     * @param arrivalLat Latitude of searched arrival point.
     * @param arrivalLng Longitude of searched arrival point.
     * @param departureTimeOfDay Departure time of day represented as RFC3339 partial-time.
     * @param departureWeekdays Departure days of week. The retrieved trips should have at least one schedule applicable on one of the departureWeekdays.
     * @param timeDelta Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius Search radius in kilometers around the arrival point.
     * @param minDepartureDate Minimum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param maxDepartureDate Maximum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param count Maximum number of returned results. If missing, all matching results are returned.
     * @return list of driver Regular Trips
     */
    List<DriverRegularTrip> getDriverRegularTrips(UUID mspId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, String departureTimeOfDay,
                                                  List<String> departureWeekdays, Integer timeDelta, Float departureRadius, Float arrivalRadius, Integer minDepartureDate, Integer maxDepartureDate, Integer count);

    /**
     * Retrieve a list of DriverJourney for a MSP.
     *
     * @param mspId msp id
     * @param departureLat Latitude of searched departure point.
     * @param departureLng Longitude of searched departure point.
     * @param arrivalLat Latitude of searched arrival point.
     * @param arrivalLng Longitude of searched arrival point.
     * @param departureDate Departure datetime using a UNIX UTC timestamp in seconds.
     * @param timeDelta Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius Search radius in kilometers around the arrival point.
     * @param count Maximum number of returned results. If missing, all matching results are returned.
     * @return list of DriverJourney for a MSP
     */
    List<DriverJourney> getDriverJourneys(UUID mspId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta,
                                          Float departureRadius, Float arrivalRadius,
                                          Integer count);

    /**
     * Retrieve a list of DriverJourney for a MSP.
     *
     * @param mspId msp id
     * @param departureLat Latitude of searched departure point.
     * @param departureLng Longitude of searched departure point.
     * @param arrivalLat Latitude of searched arrival point.
     * @param arrivalLng Longitude of searched arrival point.
     * @param departureDate Departure datetime using a UNIX UTC timestamp in seconds.
     * @param timeDelta Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius Search radius in kilometers around the arrival point.
     * @param count Maximum number of returned results. If missing, all matching results are returned.
     * @return list of DriverJourney for a MSP
     */
    List<PassengerJourney> getPassengerJourneys(UUID mspId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius, Float arrivalRadius,
                                                Integer count);
}

