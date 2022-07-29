package com.gateway.api.service.ivservice;

import com.gateway.api.model.*;
import java.util.List;
import java.util.UUID;


public interface IVService {

    /**
            * Retrieve a list of stations for a MSP.
            *
            * @param mspId Identifier of the MSP.
     * @return List of stations (static informations) for the MSP
     * 
     */
    List<Station> getStations(UUID mspId) ;



    /**
     * Retrieve a list of stations status for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of stations (dynamic informations) for the MSP
     *
     */
    List<StationStatus> getStationStatus(UUID mspId) ;

    /**
     * Retrieve a list of assets for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of assets for the MSP.
     *
     */
    List<Asset> getAssets(UUID mspId) ;


    /**
     * Retrieve a list of available assets for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @param stationId
     * @return List of available assets for the MSP.
     *
     */
    List<AssetType> getAvailableAssets(UUID mspId, String stationId);


    /**
     * Retrieve a global view.
     *
     * @return Global view.
     *
     */
    GlobalView getGlobalView() ;


    /**
     * Retrieve a global view for around me.
     * @param mspAroundMeRequest
     * @return Global view.
     *
     */
    GlobalView getAroundMe(MSPAroundMeRequest mspAroundMeRequest) ;

}
