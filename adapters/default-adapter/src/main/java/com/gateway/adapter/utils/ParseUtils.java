package com.gateway.adapter.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.adapter.utils.constant.ActionDict;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.CommonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
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
     * @param mspId
     * @throws IOException
     */
    public void parseToGatewayDTOByAction(List<Object> list, JSONObject jsonObjectResponse, MspActionDTO
            mspAction, UUID mspId) throws IOException, JSONException {

        if (!jsonObjectResponse.has("mspId")) {
            jsonObjectResponse.accumulate("mspId", mspId);
        }

        if (mspAction.isAuthentication()) {
            try {
                TokenDTO token = this.parseToken(jsonObjectResponse);
                token.setExpireAt(CustomParamUtils.tokenExpireAt(jsonObjectResponse));
                list.add(token);
            } catch (Exception e) {
                throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, TokenDTO.class.toString()));
            }
        } else {

            switch (mspAction.getAction()) {
                case ActionDict.AVAILABLE_ASSET_SEARCH:
                    try {
                        AssetType assetType = this.parseAssetType(jsonObjectResponse);
                        //add mspId for each asset
                        List<Asset> assets = assetType.getAssets();
                        if (assets != null && !assets.isEmpty()) {
                            for (Asset asset : assets) {
                                asset.setMspId(mspId);
                            }
                        }
                        assetType.setAssets(assets);

                        list.add(assetType);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, AssetType.class.toString()));
                    }
                    break;
                case ActionDict.ASSET_SEARCH:
                    try {
                        Asset asset = this.parseAsset(jsonObjectResponse);
                        list.add(asset);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Asset.class.toString()));
                    }
                    break;
                case ActionDict.STATION_SEARCH:
                    try {
                        Station station = this.parseStation(jsonObjectResponse);
                        list.add(station);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Station.class.toString()));
                    }
                    break;
                case ActionDict.PARKING_SEARCH:
                    try {
                        Parking parking = this.parseParking(jsonObjectResponse);
                        list.add(parking);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, Parking.class.toString()));
                    }
                    break;
                case ActionDict.AROUND_ME_SEARCH:
                case ActionDict.GLOBAL_VIEW_SEARCH:

                    try {
                        GlobalView globalView = this.praseGlobalView(jsonObjectResponse);

                        //add mspId for each asset
                        List<Asset> assets = globalView.getAssets();
                        if (assets != null && !assets.isEmpty()) {
                            for (Asset asset : assets) {
                                asset.setMspId(mspId);
                            }
                        }
                        //add mspId for each station
                        List<Station> stations = globalView.getStations();
                        if (stations != null && !stations.isEmpty()) {
                            for (Station station : stations) {
                                station.setMspId(mspId);
                            }
                        }
                        //add mspId for each station_status
                        List<StationStatus> stationsStatus = globalView.getStationsStatus();
                        if (stationsStatus != null && !stationsStatus.isEmpty()) {
                            for (StationStatus stationStatus : stationsStatus) {
                                stationStatus.setMspId(mspId);
                            }
                        }

                        //add mspId for each parking
                        List<Parking> parkings = globalView.getParkings();
                        if (parkings != null && !parkings.isEmpty()) {
                            for (Parking parking : parkings) {
                                parking.setMspId(mspId);
                            }
                        }

                        list.add(globalView);

                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, GlobalView.class.toString()));
                    }
                    break;

                case ActionDict.STATION_STATUS_SEARCH:
                    try {
                        StationStatus stationStatus = this.parseStationStatus(jsonObjectResponse);
                        list.add(stationStatus);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, StationStatus.class.toString()));
                    }
                    break;
                case ActionDict.MSP_ZONE_SEARCH:
                    try {
                        MSPZone mspZone = this.parseMspZone(jsonObjectResponse);
                        list.add(mspZone);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, MSPZone.class.toString()));
                    }
                    break;

                case ActionDict.VEHICLE_TYPES_SEARCH:
                    try {
                        VehicleTypes vehicleTypes = this.parseVehicleType(jsonObjectResponse);
                        list.add(vehicleTypes);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, VehicleTypes.class.toString()));
                    }
                    break;

                case ActionDict.PRICING_PLAN_SEARCH:
                    try {
                        PriceListDTO priceList = this.parsePriceList(jsonObjectResponse);
                        list.add(priceList);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PriceListDTO.class.toString()));
                    }
                    break;
                case ActionDict.DRIVER_JOURNEYS_SEARCH:
                    try {
                        DriverJourney driverJourney = this.parseDriverJourneys(jsonObjectResponse);
                        list.add(driverJourney);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, DriverJourney.class.toString()));
                    }
                    break;
                case ActionDict.PASSENGER_JOURNEYS_SEARCH:
                    try {
                        PassengerJourney passengerJourneys = this.parsePassengerJourneys(jsonObjectResponse);
                        list.add(passengerJourneys);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PassengerJourney.class.toString()));
                    }
                    break;
                case ActionDict.PASSENGER_REGULAR_TRIPS_SEARCH:
                    try {
                        PassengerRegularTrip passengerRegularTrip = this.parsePassengerRegularTrip(jsonObjectResponse);
                        list.add(passengerRegularTrip);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, PassengerRegularTrip.class.toString()));
                    }
                    break;
                case ActionDict.DRIVER_REGULAR_TRIPS_SEARCH:
                    try {
                        DriverRegularTrip driverRegularTrip = this.parseDriverRegularTrip(jsonObjectResponse);
                        list.add(driverRegularTrip);
                    } catch (Exception e) {
                        throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, DriverRegularTrip.class.toString()));
                    }
                    break;
            }

        }
    }

    /**
     * parse objectJson in MspZone
     *
     * @param jsonObjectResponse
     * @return mspZone object
     * @throws IOException
     */

    private MSPZone parseMspZone(JSONObject jsonObjectResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonObjectResponse.toString(), MSPZone.class);
    }

    /**
     * parse objectJson in AssetType
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return assetType objet
     * @throws IOException
     */
    public AssetType parseAssetType(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), AssetType.class);
    }

    /**
     * parse objectJson in Asset
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Asset objet
     * @throws IOException
     */
    public Asset parseAsset(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), Asset.class);
    }

    /**
     * parse objectJson in Station
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Station objet
     * @throws IOException
     */
    public Station parseStation(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), Station.class);
    }

    /**
     * parse objectJson in Parking
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Parking objet
     * @throws IOException
     */
    public Parking parseParking(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), Parking.class);
    }

    /**
     * parse objectJson in GlobalView
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Parking objet
     * @throws IOException
     */
    public GlobalView praseGlobalView(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), GlobalView.class);
    }


    /**
     * parse objectJson in StationStatus
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return StationStatus objet
     * @throws IOException
     */
    public StationStatus parseStationStatus(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), StationStatus.class);
    }

    /**
     * parse objectJson in TokenDTO
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Token objet
     * @throws IOException
     */
    public TokenDTO parseToken(JSONObject dataResponse) throws IOException, JSONException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), TokenDTO.class);
    }

    /**
     * parse objectJson in VehicleTypes
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return assetType objet
     * @throws IOException
     */
    public VehicleTypes parseVehicleType(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), VehicleTypes.class);
    }

    /**
     * parse objectJson in PriceList
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return assetType objet
     * @throws IOException
     */
    public PriceListDTO parsePriceList(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), PriceListDTO.class);
    }

    /**
     * parse objectJson in driver Regular Trip
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format (Carpooling Standard in this case)
     * @return DriverRegularTrip objet
     * @throws IOException
     */
    public DriverRegularTrip parseDriverRegularTrip(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), DriverRegularTrip.class);
    }

    /**
     * parse objectJson in Passenger Regular Trip
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format (Carpooling Standard in this case)
     * @return PassengerRegularTrip objet
     * @throws IOException
     */
    public PassengerRegularTrip parsePassengerRegularTrip(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), PassengerRegularTrip.class);
    }

    /**
     * parse objectJson in DriverJourney
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return DriverJourney objet
     * @throws IOException
     */
    public DriverJourney parseDriverJourneys(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), DriverJourney.class);
    }

    /**
     * parse objectJson in PassengerJourneys
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return PassengerJourneys objet
     * @throws IOException
     */
    public PassengerJourney parsePassengerJourneys(JSONObject dataResponse) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), PassengerJourney.class);
    }


}
