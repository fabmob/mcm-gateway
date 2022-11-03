package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface PartnerActionsService {


    /**
     * Add a new PartnerActionDto
     *
     * @param action PartnerActionDTO object
     * @return PartnerActionDTO information for the PartnerActionDTO added
     */

    /**
     * Add a new PartnerActionDto
     *
     * @param action PartnerActionDTO object
     * @return PartnerActionDTO information for the PartnerActionDTO added
     */


    public PartnerActionDTO addPartnerAction(PartnerActionDTO action);

    /**
     * Retrieve a list of PartnerActions transported into PartnerActionsDto
     *
     * @return List of PartnerActionsDTO
     */

    public List<PartnerActionDTO> getAllPartnerActions();

    /**
     * Update all the PartnerActions information
     *
     * @param id     Identifier of the PartnerActionsDTO
     * @param action PartnerActionsDTO object
     * @return PartnerActionsDTO information for the PartnerActionsDTO put
     */
    /**
     * Update all the PartnerActions information
     *
     * @param id     Identifier of the PartnerActionsDTO
     * @param action PartnerActionsDTO object
     * @return PartnerActionsDTO information for the PartnerActionsDTO put
     */


    public PartnerActionDTO updatePartnerAction(UUID id, PartnerActionDTO action);

    /**
     * Delete a PartnerActionsDTO
     *
     * @param id Identifier of the PartnerActionsDTO
     */

    public void deletePartnerAction(UUID id);

    /**
     * Retrieve a PartnerActionDto information.
     *
     * @param id Identifier of the PartnerActionDto
     * @return PartnerActionDto information for the PartnerActionDto
     * @throws NotFoundException not found object
     */
    public PartnerActionDTO getPartnerActionFromId(UUID id) throws NotFoundException;


    /**
     * Retrieve a PartnerActionDto information.
     *
     * @param id Identifier of the PartnerMeta
     * @return PartnerActionDto information for the PartnerActionDto
     * @throws NotFoundException not found object
     */
    public List<PartnerActionDTO> getPartnerActionFromIdPartnerMetaId(UUID id) throws NotFoundException;

}
