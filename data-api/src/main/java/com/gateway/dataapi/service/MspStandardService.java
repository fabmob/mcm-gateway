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
     * @return MspStandardDTO information for the MspStandardDTO added
     */
    MspStandardDTO addMspStandard(MspStandardDTO standardDTO) throws NotFoundException;

    /**
     * Retrieve a list of MspStandard transported into MspStandardDto
     *
     * @return List of MspStandardDTO
     */
    List<MspStandardDTO> getAllMspStandards();


    /**
     * Update all the MspStandard information
     *
     * @param id          Identifier of the MspStandardDTO
     * @param standardDTO MspStandardDTO object
     */
    void updateMspStandard(UUID id, MspStandardDTO standardDTO);

    /**
     * Delete a MspStandardDTO
     *
     * @param id Identifier of the MspStandardDTO
     */

    void deleteMspStandard(UUID id);

    /**
     * Retrieve a MspStandardDto information.
     *
     * @param id Identifier of the MspStandardDto
     * @return MspStandardDto information for the MspStandardDto
     * @throws NotFoundException not found object
     */
    MspStandardDTO getMspStandardFromId(UUID id) throws NotFoundException;

    /**
     * Get MspStandardDto from MspActions id and MspMeta id VersionStandard
     *
     * @param mspMetaId MSP ID
     * @param mspActionsId Action ID
     * @param mspActionsName Action Name
     * @param versionStandard Standard version
     * @param versionDatamapping Datamapping version
     * @return MspStandardDto
     * @throws NotFoundException not found
     */
    List<MspStandardDTO> getByCriteria(UUID mspMetaId, UUID mspActionsId, String mspActionsName, String versionStandard, String versionDatamapping, Boolean isActive) throws NotFoundException;
}
