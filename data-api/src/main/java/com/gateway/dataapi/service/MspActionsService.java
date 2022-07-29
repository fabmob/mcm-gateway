package com.gateway.dataapi.service;

import java.util.List;
import java.util.UUID;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.exception.NotFoundException;

public interface MspActionsService {


    /**
     * Add a new MspActionDto
     *
     * @param action MspActionDTO object
     * @return MspActionDTO informations for the MspActionDTO added
     */

	/**
	 * Add a new MspActionDto
	 * 
	 * @param action MspActionDTO object
	 * @return MspActionDTO informations for the MspActionDTO added
	 */


    public MspActionDTO addMspAction(MspActionDTO action);

    /**
     * Retrieve a list of MspActions transported into MspActionsDto
     *
     * @return List of MspActionsDTO
     */

    public List<MspActionDTO> getAllMspActions();

    /**
     * Update all the MspActions informations
     *
     * @param id     Identifier of the MspActionsDTO
     * @param action MspActionsDTO object
     * @return MspActionsDTO informations for the MspActionsDTO puted
     */
	/**
	 * Update all the MspActions informations
	 * 
	 * @param id Identifier of the MspActionsDTO
	 * @param action MspActionsDTO object
	 * @return MspActionsDTO informations for the MspActionsDTO puted
	 */


    public MspActionDTO updateMspAction(UUID id, MspActionDTO action);

    /**
     * Delete a MspActionsDTO
     *
     * @param id Identifier of the MspActionsDTO
     */

    public void deleteMspAction(UUID id);

    /**
     * Retrieve a MspActionDto informations.
     *
     * @param id Identifier of the MspActionDto
     * @return MspActionDto informations for the MspActionDto
     * @throws NotFoundException not found object
     */
    public MspActionDTO getMspActionFromId(UUID id) throws NotFoundException;


	/**
	 * Retrieve a MspActionDto informations.
	 *
	 * @param id Identifier of the MspMeta
	 * @return MspActionDto informations for the MspActionDto
	 * @throws NotFoundException not found object
	 */
	public List<MspActionDTO> getMspActionFromIdMspMetaId(UUID id) throws NotFoundException;

}
