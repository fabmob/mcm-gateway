package com.gateway.api.service.ivservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.model.MSPMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.service.mspservice.MSPService;
import com.gateway.api.util.constant.GatewayApiPathDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static com.gateway.api.util.constant.GatewayApiPathDict.*;
import static com.gateway.api.util.constant.GatewayMessageDict.*;


@Service
@Slf4j
public class IVServiceImpl implements IVService {
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MSPService mspService;

    private final String correlationId = String.valueOf(CommonUtils.setHeader().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private ErrorMessages errorMessages;

    @Override
    public List<Station> getStations(UUID mspId, Float lon, Float lat, Float rad) {

        Map<String, String> params = new HashMap<>();
        if (lon != null) {
            params.put("lon", String.valueOf(lon));
        }
        if (lat != null) {
            params.put("lat", String.valueOf(lat));
        }
        if (rad != null) {
            params.put("rad", String.valueOf(rad));
        }

        List<Object> response = (List<Object>) this.getRooting(mspId, STATION_SEARCH, Optional.empty(), params);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_STATIONS_STATUS_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<Station> stations = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<Station> convertedResponse = mapper.convertValue(response, new TypeReference<List<Station>>() {
            });
            for (Station station : convertedResponse) {
                stations.add(station);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GatewayApiPathDict.GET_MSP_STATIONS_PATH, mspId);
        }
        return stations;
    }

    @Override
    public List<StationStatus> getStationStatus(UUID mspId, String stationId) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(stationId)) {
            params.put(STATION_ID, stationId);
        }

        List<Object> response = (List<Object>) this.getRooting(mspId, STATION_STATUS_SEARCH, Optional.empty(), params);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_STATIONS_STATUS_PATH, MSP_ID, String.valueOf(mspId))));
        }

        List<StationStatus> stationStatuses = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<StationStatus> convertedResponse = mapper.convertValue(response, new TypeReference<List<StationStatus>>() {
            });
            for (StationStatus stationStatus : convertedResponse) {
                if (stationStatus.getStationId().equals(stationId) || stationId == null) {
                    stationStatuses.add(stationStatus);
                }
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_MSP_STATIONS_STATUS_PATH, mspId);
        }
        return stationStatuses;
    }

    @Override
    public List<Asset> getAssets(UUID mspId) {
        List<Object> response = (List<Object>) this.getRooting(mspId, ASSET_SEARCH, Optional.empty(), null);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_STATIONS_STATUS_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<Asset> assets = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Asset> convertedResponse = mapper.convertValue(response, new TypeReference<List<Asset>>() {
            });
            for (Asset asset : convertedResponse) {
                assets.add(asset);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_ASSETS_BY_MSP_ID, mspId);
        }
        return assets;

    }

    @Override
    public List<AssetType> getAvailableAssets(UUID mspId, String stationId, Float lon, Float lat, Float rad) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(stationId)) {
            params.put(STATION_ID, String.valueOf(stationId));
        }
        if (lon != null) {
            params.put("lon", String.valueOf(lon));
        }
        if (lat != null) {
            params.put("lat", String.valueOf(lat));
        }
        if (rad != null) {
            params.put("rad", String.valueOf(rad));
        }

        List<Object> response = (List<Object>) this.getRooting(mspId, AVAILABLE_ASSET_SEARCH, Optional.empty(), params);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_AVAILABLE_ASSETS_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<AssetType> assetTypes = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<AssetType> convertedResponse = mapper.convertValue(response, new TypeReference<List<AssetType>>() {
            });
            for (AssetType assetType : convertedResponse) {
                if (assetType.getStationId().equals(stationId) || stationId == null) {
                    assetTypes.add(assetType);
                }
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_MSPS_PATH + GET_MSP_AVAILABLE_ASSETS_PATH, mspId);
        }
        return assetTypes;
    }

    @Override
    public GlobalView getGlobalView() throws IOException, InterruptedException {
        List<MSPMeta> mspMetas = mspService.getMSPsMeta();
        GlobalView globalView = new GlobalView();
        List<UUID> mspIds = new ArrayList<>();
        mspMetas.stream().forEach(mspMeta -> {
            mspIds.add(mspMeta.getMspId());
        });
        for (int i = 0; i < mspIds.size(); i++) {
            List<Object> response = (List<Object>) this.getRooting(mspIds.get(i), GLOBAL_VIEW_SEARCH, Optional.empty(), null);
            if (response != null) {
                ObjectMapper mapper = new ObjectMapper();
                List<GlobalView> globalViews = mapper.convertValue(response, new TypeReference<List<GlobalView>>() {
                });
                globalViews.stream().forEach(element -> {
                    globalView.getParkings().addAll(element.getParkings());
                    globalView.getAssets().addAll(element.getAssets());
                    globalView.getStationsStatus().addAll(element.getStationsStatus());
                    globalView.getStations().addAll(element.getStations());
                });
            }
        }
        return globalView;
    }

    /**
     * Validate Body request
     *
     * @param mspAroundMeRequest
     * @return
     */
    private Map<String, Object> validateBodyRequest(MSPAroundMeRequest mspAroundMeRequest) {
        Map<String, Object> body = new HashMap<String, Object>();
        if (mspAroundMeRequest.getMspIds() != null) {
            body.put("mspIds", mspAroundMeRequest.getMspIds());
        }
        if (mspAroundMeRequest.getLat() != null) {
            body.put("lat", mspAroundMeRequest.getLat());

        }
        if (mspAroundMeRequest.getLon() != null) {
            body.put("lon", mspAroundMeRequest.getLon());
        }
        if (mspAroundMeRequest.getRadius() != null) {
            body.put("radius", mspAroundMeRequest.getRadius());
        }
        if (mspAroundMeRequest.getMaxResult() != null) {
            body.put("maxResult", mspAroundMeRequest.getMaxResult());
        }
        return body;
    }

    @Override
    public GlobalView getAroundMe(MSPAroundMeRequest mspAroundMeRequest) {
        Map<String, Object> body = this.validateBodyRequest(mspAroundMeRequest);
        GlobalView globalView = new GlobalView();
        //Infos around me for selected msps
        List<UUID> mspIds = mspAroundMeRequest.getMspIds();

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < mspIds.size(); i++) {
            List<Object> response = (List<Object>) this.getRooting(mspIds.get(i), AROUND_ME_SEARCH, Optional.of(body), null);
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<GlobalView> globalViews = mapper.convertValue(response, new TypeReference<List<GlobalView>>() {
                });
                globalViews.stream().forEach(element -> {
                    globalView.getParkings().addAll(element.getParkings());
                    globalView.getAssets().addAll(element.getAssets());
                    globalView.getStationsStatus().addAll(element.getStationsStatus());
                    globalView.getStations().addAll(element.getStations());
                });
            } catch (Exception e) {
                this.exceptionHandler(e, GET_MSPS_PATH + GET_MSPS_AROUND_ME_PATH + FOR_MSP_WITH_ID_MSP_ID, mspIds.get(i));
            }
        }
        return globalView;
    }

    @Override
    public List<VehicleTypes> getVehicleTypes(UUID mspId) {

        try {
            List<Object> response = (List<Object>) this.getRooting(mspId, VEHICLE_TYPES_SEARCH, Optional.empty(), null);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSPS_PATH + GET_MSP_VEHICLE_TYPES_PATH, MSP_ID, String.valueOf(mspId))));
            }
            ObjectMapper mapper = new ObjectMapper();
            List<VehicleTypes> responseList = mapper.convertValue(response, new TypeReference<List<VehicleTypes>>() {
            });
            List<VehicleTypes> vehicleTypes = new ArrayList<>();
            for (VehicleTypes types : responseList) {
                vehicleTypes.add(types);
            }
            return vehicleTypes;
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSPS_PATH + GET_MSP_VEHICLE_TYPES_PATH, MSP_ID, String.valueOf(mspId))));
        }

    }

    @Override
    public List<PriceList> getPricingPlan(UUID mspId, String stationId) {
        try {
            Map<String, String> params = new HashMap<>();
            if (StringUtils.isNotBlank(stationId)) {
                params.put(STATION_ID, stationId);
            }

            List<Object> response = (List<Object>) this.getRooting(mspId, PRICING_PLAN_SEARCH, Optional.empty(), params);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_PRICING_PLAN_PATH, MSP_ID, String.valueOf(mspId), STATION_ID, stationId)));
            }
            ObjectMapper mapper = new ObjectMapper();
            List<PriceList> responseList = mapper.convertValue(response, new TypeReference<List<PriceList>>() {
            });

            List<PriceList> priceList = new ArrayList<>();
            for (PriceList list : responseList) {
                priceList.add(list);
            }
            return priceList;
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_PRICING_PLAN_PATH, MSP_ID, String.valueOf(mspId), STATION_ID, stationId)));
        }
    }

    /**
     * Call routing
     *
     * @param mspId
     * @param actionName
     * @param body
     * @param params
     * @return
     */
    private Object getRooting(UUID mspId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {
        String mspMetaIdValue = mspId != null ? mspId.toString() : null;
        Object mspBusinessResponse = null;
        String urlCall = routingApiUri + CommonUtils.placeholderFormat(GET_MSP_ID_PATH, MSP_ID, mspMetaIdValue
                + GET_ACTION_NAME_PATH, ACTION_NAME, actionName);
        String urlTemplate = CommonUtils.constructUrlTemplate(urlCall, params);
        log.debug(LOG_URL_CALL, urlTemplate);
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(urlTemplate, body, Object.class);
            if (response.getBody() != null) {
                mspBusinessResponse = Objects.requireNonNull(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new NotFoundException(error.getDescription());

        } catch (HttpServerErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new InternalException(error.getDescription());

        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall));
        }

        return mspBusinessResponse;
    }


    /**
     * Return a customed exception with initial error message if it exists or a customed global one
     *
     * @param e
     * @param ServiceCalledMsg
     * @param mspId
     */
    private void exceptionHandler(Exception e, String ServiceCalledMsg, UUID mspId) {
        if (e.getMessage() != null) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(ServiceCalledMsg, MSP_ID, String.valueOf(mspId))));
        }
    }

    private Map<String, String> getJourneysParams(Float departureLat, Float departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius, Float arrivalRadius, Integer count) {
        Map<String, String> journeysParams = new HashMap<>();
        if (departureLat != null) {
            journeysParams.put("departureLat", String.valueOf(departureLat));
        }
        if (departureLng != null) {
            journeysParams.put("departureLng", String.valueOf(departureLng));
        }
        if (arrivalLat != null) {
            journeysParams.put("arrivalLat", String.valueOf(arrivalLat));
        }
        if (arrivalLng != null) {
            journeysParams.put("arrivalLng", String.valueOf(arrivalLng));
        }
        if (departureDate != null) {
            journeysParams.put("departureDate", String.valueOf(departureDate));
        }
        if (timeDelta != null) {
            journeysParams.put("timeDelta", String.valueOf(timeDelta));
        }
        if (departureRadius != null) {
            journeysParams.put("departureRadius", String.valueOf(departureRadius));
        }
        if (arrivalRadius != null) {
            journeysParams.put("arrivalRadius", String.valueOf(arrivalRadius));
        }
        if (count != null) {
            journeysParams.put("count", String.valueOf(count));
        }
        return journeysParams;
    }

    @Override
    public List<DriverJourney> getDriverJourneys(UUID mspId, Float departureLat, Float departureLng,
                                                 Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta,
                                                 Float departureRadius, Float arrivalRadius, Integer count) {

        Map<String, String> journeysParams = this.getJourneysParams(departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);

        List<Object> response = (List<Object>) this.getRooting(mspId, DRIVER_JOURNEYS_SEARCH, Optional.empty(), journeysParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_DRIVER_JOURNEY_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<DriverJourney> driverJourneys = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<DriverJourney> convertedResponse = mapper.convertValue(response, new TypeReference<List<DriverJourney>>() {
            });
            for (DriverJourney driverJourney : convertedResponse) {
                driverJourneys.add(driverJourney);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GatewayApiPathDict.GET_MSP_DRIVER_JOURNEY_PATH, mspId);
        }
        return driverJourneys;
    }


    @Override
    public List<PassengerJourney> getPassengerJourneys(UUID mspId, Float departureLat, Float departureLng, Float arrivalLat,
                                                       Float arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius,
                                                       Float arrivalRadius, Integer count) {

        Map<String, String> journeysParams = this.getJourneysParams(departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);

        List<Object> response = (List<Object>) this.getRooting(mspId, PASSENGER_JOURNEYS_SEARCH, Optional.empty(), journeysParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_DRIVER_JOURNEY_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<PassengerJourney> passengerJourneys = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<PassengerJourney> convertedResponse = mapper.convertValue(response, new TypeReference<List<PassengerJourney>>() {
            });
            for (PassengerJourney passengerJourneys1 : convertedResponse) {
                passengerJourneys.add(passengerJourneys1);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GatewayApiPathDict.GET_MSP_PASSENGER_JOURNEY_PATH, mspId);
        }
        return passengerJourneys;
    }


    private Map<String, String> getRegularTripsParams(Float departureLat, Float departureLng,
                                                      Float arrivalLat, Float arrivalLng,
                                                      String departureTimeOfDay,
                                                      List<String> departureWeekdays, Integer timeDelta,
                                                      Float departureRadius, Float arrivalRadius,
                                                      Integer minDepartureDate, Integer maxDepartureDate, Integer count) {
        Map<String, String> regularTripsParams = new HashMap<>();
        if (departureLat != null) {
            regularTripsParams.put("departureLat", String.valueOf(departureLat));
        }
        if (departureLng != null) {
            regularTripsParams.put("departureLng", String.valueOf(departureLng));
        }
        if (arrivalLat != null) {
            regularTripsParams.put("arrivalLat", String.valueOf(arrivalLat));
        }
        if (arrivalLng != null) {
            regularTripsParams.put("arrivalLng", String.valueOf(arrivalLng));
        }
        if (departureTimeOfDay != null) {
            regularTripsParams.put("departureTimeOfDay", String.valueOf(departureTimeOfDay));
        }

        if (departureWeekdays != null) {
            regularTripsParams.put("departureWeekdays", String.join(",", departureWeekdays));
        }
        if (timeDelta != null) {
            regularTripsParams.put("timeDelta", String.valueOf(timeDelta));
        }
        if (departureRadius != null) {
            regularTripsParams.put("departureRadius", String.valueOf(departureRadius));
        }
        if (arrivalRadius != null) {
            regularTripsParams.put("arrivalRadius", String.valueOf(arrivalRadius));
        }
        if (minDepartureDate != null) {
            regularTripsParams.put("minDepartureDate", String.valueOf(minDepartureDate));
        }
        if (maxDepartureDate != null) {
            regularTripsParams.put("maxDepartureDate", String.valueOf(maxDepartureDate));
        }
        if (count != null) {
            regularTripsParams.put("count", String.valueOf(count));
        }
        return regularTripsParams;
    }

    @Override
    public List<PassengerRegularTrip> getPassengerRegularTrips(UUID mspId, Float departureLat, Float departureLng,
                                                        Float arrivalLat, Float arrivalLng,
                                                        String departureTimeOfDay,
                                                        List<String> departureWeekdays, Integer timeDelta,
                                                        Float departureRadius, Float arrivalRadius,
                                                        Integer minDepartureDate, Integer maxDepartureDate, Integer count) {

        Map<String, String> regularTripsParams = this.getRegularTripsParams(departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        List<Object> response = (List<Object>) this.getRooting(mspId, PASSENGER_REGULAR_TRIPS_SEARCH, Optional.empty(), regularTripsParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_DRIVER_JOURNEY_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<PassengerRegularTrip> passengerRegularTrips = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<PassengerRegularTrip> convertedResponse = mapper.convertValue(response, new TypeReference<List<PassengerRegularTrip>>() {
            });
            for (PassengerRegularTrip passengerRegularTrip : convertedResponse) {
                passengerRegularTrips.add(passengerRegularTrip);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_MSP_PASSENGER_REGULAR_TRIPS_PATH, mspId);
        }
        return passengerRegularTrips;
    }


    @Override
    public List<DriverRegularTrip> getDriverRegularTrips(UUID mspId, Float departureLat, Float departureLng,
                                                  Float arrivalLat, Float arrivalLng,
                                                  String departureTimeOfDay, List<String> departureWeekdays,
                                                  Integer timeDelta,
                                                  Float departureRadius, Float arrivalRadius,
                                                  Integer minDepartureDate, Integer maxDepartureDate, Integer count) {

        Map<String, String> regularTripsParams = this.getRegularTripsParams(departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        List<Object> response = (List<Object>) this.getRooting(mspId, DRIVER_REGULAR_TRIPS_SEARCH, Optional.empty(), regularTripsParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_MSP_DRIVER_JOURNEY_PATH, MSP_ID, String.valueOf(mspId))));
        }
        List<DriverRegularTrip> driverRegularTrips = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<DriverRegularTrip> convertedResponse = mapper.convertValue(response, new TypeReference<List<DriverRegularTrip>>() {
            });
            for (DriverRegularTrip driverRegularTrip : convertedResponse) {
                driverRegularTrips.add(driverRegularTrip);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_MSP_DRIVER_REGULAR_TRIPS_PATH, mspId);
        }
        return driverRegularTrips;
    }


}
