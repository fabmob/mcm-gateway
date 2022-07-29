package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface MspStandardService {

    /**
     * Add a new MspStandardDto
     *
     * @param standardDTO MspStandardDTO object
     * @return MspStandardDTO informations for the MspStandardDTO added
     */
    public MspStandardDTO addMspStandard(MspStandardDTO standardDTO) throws NotFoundException;

    /**
     * Retrieve a list of MspStandard transported into MspStandardDto
     *
     * @return List of MspStandardDTO
     */
    public List<MspStandardDTO> getAllMspStandards();


    /**
     * Update all the MspStandard informations
     *
     * @param id          Identifier of the MspStandardDTO
     * @param standardDTO MspStandardDTO object
     * @return MspStandardDTO informations for the MspStandardDTO puted
     */
    public MspStandardDTO updateMspStandard(UUID id, MspStandardDTO standardDTO);

    /**
     * Delete a MspStandardDTO
     *
     * @param id Identifier of the MspStandardDTO
     */

    public void deleteMspStandard(UUID id);

    /**
     * Retrieve a MspStandardDto informations.
     *
     * @param id Identifier of the MspStandardDto
     * @return MspStandardDto informations for the MspStandardDto
     * @throws NotFoundException not found object
     */
    public MspStandardDTO getMspStandardFromId(UUID id) throws NotFoundException;

    /**
     * Get MspStandardDto from MspActions id and MspMeta id VersionStandard VersionStandard
     *
     * @param mspMetaId
     * @param mspActionsId
     * @param versionStandard
     * @param versionDatamapping
     * @return MspStandardDto
     * @throws NotFoundException
     */
    public List<MspStandardDTO> getByCriteria(UUID mspMetaId, UUID mspActionsId, String versionStandard, String versionDatamapping) throws NotFoundException;
}
