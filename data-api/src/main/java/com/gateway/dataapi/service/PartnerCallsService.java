package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.PartnerCallsDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface PartnerCallsService {

    /**
     * Add a new PartnerCallsDto
     *
     * @param call PartnerCallsDTO object
     * @return PartnerCallsDTO information for the PartnerCallsDTO added
     */
    public PartnerCallsDTO addPartnerCalls(PartnerCallsDTO call);

    /**
     * Retrieve a list of PartnerCalls transported into PartnerCallsDto
     *
     * @return List of PartnerCallsDTO
     */

    public List<PartnerCallsDTO> getAllPartnerCalls();

    /**
     * Update all the PartnerCalls information
     *
     * @param id   Identifier of the PartnerCallsDTO
     * @param call PartnerCallsDTO object
     * @return PartnerCallsDTO information for the PartnerCallsDTO put
     */

    public PartnerCallsDTO updatePartnerCalls(UUID id, PartnerCallsDTO call);

    /**
     * Delete a PartnerCallsDTO
     *
     * @param id Identifier of the PartnerCallsDTO
     */

    public void deletePartnerCalls(UUID id);

    /**
     * Retrieve a PartnerCallsDto information.
     *
     * @param id Identifier of the PartnerCallsDto
     * @return PartnerCallsDto information for the PartnerCallsDto
     * @throws NotFoundException not found object
     */

    public PartnerCallsDTO getPartnerCallsFromId(UUID id) throws NotFoundException;

    /**
     * Get PartnerCallsDto from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerCallsDto
     * @throws NotFoundException
     */
    public List<PartnerCallsDTO> getByActionId(UUID id) throws NotFoundException;

}
