package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.MspCallsDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface MspCallsService {

    /**
     * Add a new MspCallsDto
     *
     * @param call MspCallsDTO object
     * @return MspCallsDTO informations for the MspCallsDTO added
     */
    public MspCallsDTO addMspCalls(MspCallsDTO call);

    /**
     * Retrieve a list of MspCalls transported into MspCallsDto
     *
     * @return List of MspCallsDTO
     */

    public List<MspCallsDTO> getAllMspCalls();

    /**
     * Update all the MspCalls informations
     *
     * @param id   Identifier of the MspCallsDTO
     * @param call MspCallsDTO object
     * @return MspCallsDTO informations for the MspCallsDTO puted
     */

    public MspCallsDTO updateMspCalls(UUID id, MspCallsDTO call);

    /**
     * Delete a MspCallsDTO
     *
     * @param id Identifier of the MspCallsDTO
     */

    public void deleteMspCalls(UUID id);

    /**
     * Retrieve a MspCallsDto informations.
     *
     * @param id Identifier of the MspCallsDto
     * @return MspCallsDto informations for the MspCallsDto
     * @throws NotFoundException not found object
     */

    public MspCallsDTO getMspCallsFromId(UUID id) throws NotFoundException;

    /**
     * Get MspCallsDto from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return MspCallsDto
     * @throws NotFoundException
     */
    public List<MspCallsDTO> getByActionId(UUID id) throws NotFoundException;

    ;

}
