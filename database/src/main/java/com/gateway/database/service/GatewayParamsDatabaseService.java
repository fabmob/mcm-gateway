package com.gateway.database.service;

import com.gateway.database.model.GatewayParams;

import java.util.List;


public interface GatewayParamsDatabaseService {

    /**
     * Add a new GatewayParams
     *
     * @param gatewayParams GatewayParams object
     * @return GatewayParams information of the added GatewayParams
     */
    public GatewayParams addGatewayParams(GatewayParams gatewayParams);

    /**
     * Retrieve  list of GatewayParams
     *
     * @return List of GatewayParams
     */

    public List<GatewayParams> getAllGatewayParams();


    /**
     * Get a GatewayParams by its paramKey
     *
     * @param paramKey Identifier of the GatewayParams
     * @return PartnerCalls information for the PartnerCalls
     */

    public GatewayParams findGatewayParamsByParamKey(String paramKey);

    /**
     * Update a GatewayParams
     *
     * @param paramKey      Identifier of the GatewayParams
     * @param gatewayParams GatewayParams object
     * @return GatewayParams information of the GatewayParams updated
     */
    public GatewayParams updateGatewayParams(String paramKey, GatewayParams gatewayParams);

    /**
     * Delete a GatewayParams
     *
     * @param paramKey Identifier of the GatewayParams to delete
     */

    public void deleteGatewayParams(String paramKey);


}
