package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.GatewayParamsDTO;

import java.util.List;

public interface GatewayParamsService {
    /**
     * Add a new GatewayParamsDTO
     *
     * @param gatewayParamsDTO GatewayParamsDTO object
     * @return GatewayParamsDTO information of the added GatewayParamsDTO
     */
    public GatewayParamsDTO addGatewayParamsDTO(GatewayParamsDTO gatewayParamsDTO);

    /**
     * Retrieve  list of GatewayParamsDTO
     *
     * @return List of GatewayParamsDTO
     */

    public List<GatewayParamsDTO> getAllGatewayParamsDTO();


    /**
     * Get a GatewayParamsDTO by its paramKey
     *
     * @param paramKey Identifier of the GatewayParamsDTO
     * @return PartnerCalls information for the PartnerCalls
     */

    public GatewayParamsDTO findGatewayParamsDTOByParamKey(String paramKey);

    /**
     * Update a GatewayParamsDTO
     *
     * @param paramKey         Identifier of the GatewayParamsDTO
     * @param gatewayParamsDTO GatewayParamsDTO object
     * @return GatewayParamsDTO information of the GatewayParamsDTO updated
     */
    public GatewayParamsDTO updateGatewayParamsDTO(String paramKey, GatewayParamsDTO gatewayParamsDTO);

    /**
     * Delete a GatewayParamsDTO
     *
     * @param paramKey Identifier of the GatewayParamsDTO to delete
     */

    public void deleteGatewayParamsDTO(String paramKey);

}
