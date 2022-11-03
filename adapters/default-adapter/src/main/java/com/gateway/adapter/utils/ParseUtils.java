package com.gateway.adapter.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.CommonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;


/**
 * class to process all parsing operations after decoding the response of the msp
 */
@Component
public class ParseUtils {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * parse objectJson in gateway dto based on action
     *
     * @param list               list of objects , contains object in gateway format
     * @param jsonObjectResponse jsonObjectResponse to parse in gateway format
     * @param mspAction          mspAction
     * @param partnerId          partner unique identifier
     */
    public void parseToGatewayDTOByAction(List<Object> list, JSONObject jsonObjectResponse, PartnerActionDTO
            mspAction, UUID partnerId) throws JSONException {

        if (!jsonObjectResponse.has("partnerId")) {
            jsonObjectResponse.accumulate("partnerId", partnerId);
        }

        switch (Objects.requireNonNull(mspAction.getAction())) {
            case AVAILABLE_ASSET_SEARCH:
                try {
                    AssetType assetType = (AssetType) this.parseObjectToClass(jsonObjectResponse, AssetType.class);
                    //add mspId for each asset
                    List<Asset> assets = assetType.getAssets();
                    if (assets != null && !assets.isEmpty()) {
                        for (Asset asset : assets) {
                            asset.setPartnerId(partnerId);
                        }
                    }
                    assetType.setAssets(assets);

                    list.add(assetType);
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, AssetType.class.toString()));
                }
                break;
            case ASSET_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, Asset.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Asset.class.toString()));
                }
                break;
            case STATION_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, Station.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Station.class.toString()));
                }
                break;
            case PARKING_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, Parking.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Parking.class.toString()));
                }
                break;
            case AROUND_ME_SEARCH:

                try {
                    GlobalView globalView = (GlobalView) this.parseObjectToClass(jsonObjectResponse, GlobalView.class);

                    //add mspId for each asset
                    List<Asset> assets = globalView.getAssets();
                    if (assets != null && !assets.isEmpty()) {
                        for (Asset asset : assets) {
                            asset.setPartnerId(partnerId);
                        }
                    }
                    //add mspId for each station
                    List<Station> stations = globalView.getStations();
                    if (stations != null && !stations.isEmpty()) {
                        for (Station station : stations) {
                            station.setPartnerId(partnerId);
                        }
                    }
                    //add mspId for each station_status
                    List<StationStatus> stationsStatus = globalView.getStationsStatus();
                    if (stationsStatus != null && !stationsStatus.isEmpty()) {
                        for (StationStatus stationStatus : stationsStatus) {
                            stationStatus.setPartnerId(partnerId);
                        }
                    }

                    //add mspId for each parking
                    List<Parking> parkings = globalView.getParkings();
                    if (parkings != null && !parkings.isEmpty()) {
                        for (Parking parking : parkings) {
                            parking.setPartnerId(partnerId);
                        }
                    }

                    list.add(globalView);

                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, GlobalView.class.toString()));
                }
                break;

            case STATION_STATUS_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, StationStatus.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, StationStatus.class.toString()));
                }
                break;
            case MSP_ZONE_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, PartnerZone.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PartnerZone.class.toString()));
                }
                break;

            case VEHICLE_TYPES_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, VehicleTypes.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, VehicleTypes.class.toString()));
                }
                break;

            case PRICING_PLAN_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, PriceListDTO.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PriceListDTO.class.toString()));
                }
                break;
            case DRIVER_JOURNEYS_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, DriverJourney.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, DriverJourney.class.toString()));
                }
                break;
            case PASSENGER_JOURNEYS_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, PassengerJourney.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PassengerJourney.class.toString()));
                }
                break;
            case PASSENGER_REGULAR_TRIPS_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, PassengerRegularTrip.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PassengerRegularTrip.class.toString()));
                }
                break;
            case DRIVER_REGULAR_TRIPS_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, DriverRegularTrip.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, DriverRegularTrip.class.toString()));
                }
                break;
            case CARPOOLING_BOOK:
            case CARPOOLING_BOOKING_SEARCH:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, Booking.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Booking.class.toString()));
                }
                break;
            case CARPOOLING_PATCH_BOOKING:
            case STATUS:
            case PING:
                break;
            case SEND_MESSAGE:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, Message.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Message.class.toString()));
                }
                break;
            case BOOKING_EVENT:
                try {
                    list.add(this.parseObjectToClass(jsonObjectResponse, CarpoolBookingEvent.class));
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, CarpoolBookingEvent.class.toString()));
                }
                break;
            default:
                throw new NotFoundException(CommonUtils.placeholderFormat(UNKNOWN_ACTION, ACTION_PLACEHOLDER, mspAction.getAction().value));
        }


    }

    /**
     * Cast the response object to the expected type
     *
     * @param jsonObjectResponse Response body
     * @param type               Class to cast the Object
     * @return Cast object to the type
     * @throws IOException if the function cannot convert to the expected type
     */
    private Object parseObjectToClass(JSONObject jsonObjectResponse, Class<?> type) throws IOException {
        objectMapper = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonObjectResponse.toString(), type);
    }


}
