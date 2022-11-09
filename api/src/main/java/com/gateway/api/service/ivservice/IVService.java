package com.gateway.api.service.ivservice;

import com.gateway.api.model.PriceList;
import com.gateway.commonapi.dto.api.*;

import java.util.List;
import java.util.UUID;


public interface IVService {

    /**
     * Retrieve a list of stations for a partner.
     *
     * @param partnerId Identifier of the partner.
     * @param lon
     * @param lat
     * @param rad
     * @return List of stations (static information) for the partner
     */
    List<Station> getStations(UUID partnerId, Float lon, Float lat, Float rad);


    /**
     * Retrieve a list of stations status for a partner.
     *
     * @param partnerId Identifier of the partner.
     * @param stationId
     * @return List of stations (dynamic information) for the partner
     */
    List<StationStatus> getStationStatus(UUID partnerId, String stationId);

    /**
     * Retrieve a list of assets for a partner.
     *
     * @param partnerId Identifier of the partner.
     * @return List of assets for the partner.
     */
    List<Asset> getAssets(UUID partnerId);


    /**
     * Retrieve a list of available assets for a partner.
     *
     * @param partnerId Identifier of the partner.
     * @param stationId
     * @param lon
     * @param lat
     * @param rad
     * @return List of available assets for the partner.
     */
    List<AssetType> getAvailableAssets(UUID partnerId, String stationId, Float lon, Float lat, Float rad);


    /**
     * Retrieve a global view.
     *
     * @return Global view.
     */
    GlobalView getGlobalView();


    /**
     * Retrieve a global view for around me.
     *
     * @param partnerAroundMeRequest
     * @return Global view.
     */
    GlobalView getAroundMe(PartnerAroundMeRequest partnerAroundMeRequest);

    /**
     * Retrieve all vehicle types of an partner
     *
     * @param partnerId
     * @return
     */
    List<VehicleTypes> getVehicleTypes(UUID partnerId);

    /**
     * Retrieve all pricing plans of an partner
     *
     * @param partnerId
     * @return
     */
    List<PriceList> getPricingPlan(UUID partnerId, String stationId);

    /**
     * Retrieve all Passenger Regular Trips
     *
     * @param departureLat       Latitude of searched departure point.
     * @param departureLng       Longitude of searched departure point.
     * @param arrivalLat         Latitude of searched arrival point.
     * @param arrivalLng         Longitude of searched arrival point.
     * @param departureTimeOfDay Departure time of day represented as RFC3339 partial-time.
     * @param departureWeekdays  Departure days of week. The retrieved trips should have at least one schedule applicable on one of the departureWeekdays.
     * @param timeDelta          Time margin in seconds.
     * @param departureRadius    Search radius in kilometers around the departure point.
     * @param arrivalRadius      Search radius in kilometers around the arrival point.
     * @param minDepartureDate   Minimum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param maxDepartureDate   Maximum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param count              Maximum number of returned results. If missing, all matching results are returned.
     * @return list of Passenger Regular Trips
     */
    List<PassengerRegularTrip> getPassengerRegularTrips(UUID partnerId, Float departureLat, Float departureLng, Float arrivalLat,
                                                        Float arrivalLng, String departureTimeOfDay, List<String> departureWeekdays, Integer timeDelta,
                                                        Float departureRadius, Float arrivalRadius, Integer minDepartureDate, Integer maxDepartureDate, Integer count);


    /**
     * Retrieve all Driver Regular Trips
     *
     * @param departureLat       Latitude of searched departure point.
     * @param departureLng       Longitude of searched departure point.
     * @param arrivalLat         Latitude of searched arrival point.
     * @param arrivalLng         Longitude of searched arrival point.
     * @param departureTimeOfDay Departure time of day represented as RFC3339 partial-time.
     * @param departureWeekdays  Departure days of week. The retrieved trips should have at least one schedule applicable on one of the departureWeekdays.
     * @param timeDelta          Time margin in seconds.
     * @param departureRadius    Search radius in kilometers around the departure point.
     * @param arrivalRadius      Search radius in kilometers around the arrival point.
     * @param minDepartureDate   Minimum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param maxDepartureDate   Maximum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.
     * @param count              Maximum number of returned results. If missing, all matching results are returned.
     * @return list of driver Regular Trips
     */
    List<DriverRegularTrip> getDriverRegularTrips(UUID partnerId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, String departureTimeOfDay,
                                                  List<String> departureWeekdays, Integer timeDelta, Float departureRadius, Float arrivalRadius, Integer minDepartureDate, Integer maxDepartureDate, Integer count);

    /**
     * Retrieve a list of DriverJourney for a partner.
     *
     * @param partnerId       partner id
     * @param departureLat    Latitude of searched departure point.
     * @param departureLng    Longitude of searched departure point.
     * @param arrivalLat      Latitude of searched arrival point.
     * @param arrivalLng      Longitude of searched arrival point.
     * @param departureDate   Departure datetime using a UNIX UTC timestamp in seconds.
     * @param timeDelta       Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius   Search radius in kilometers around the arrival point.
     * @param count           Maximum number of returned results. If missing, all matching results are returned.
     * @return list of DriverJourney for a partner
     */
    List<DriverJourney> getDriverJourneys(UUID partnerId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta,
                                          Float departureRadius, Float arrivalRadius,
                                          Integer count);

    /**
     * Retrieve a list of DriverJourney for a partner.
     *
     * @param partnerId       partner id
     * @param departureLat    Latitude of searched departure point.
     * @param departureLng    Longitude of searched departure point.
     * @param arrivalLat      Latitude of searched arrival point.
     * @param arrivalLng      Longitude of searched arrival point.
     * @param departureDate   Departure datetime using a UNIX UTC timestamp in seconds.
     * @param timeDelta       Time margin in seconds.
     * @param departureRadius Search radius in kilometers around the departure point.
     * @param arrivalRadius   Search radius in kilometers around the arrival point.
     * @param count           Maximum number of returned results. If missing, all matching results are returned.
     * @return list of DriverJourney for a partner
     */
    List<PassengerJourney> getPassengerJourneys(UUID partnerId, Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius, Float arrivalRadius,
                                                Integer count);

    /**
     * Post a message to the owner of a journey.
     *
     * @param partnerId
     * @param message
     */
    void postMessage(UUID partnerId, Message message);

}

