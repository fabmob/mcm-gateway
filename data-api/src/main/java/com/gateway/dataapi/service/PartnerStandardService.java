package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface PartnerStandardService {

    /**
     * Add a new PartnerStandardDto
     *
     * @param standardDTO PartnerStandardDTO object
     * @return PartnerStandardDTO information for the PartnerStandardDTO added
     */
    PartnerStandardDTO addPartnerStandard(PartnerStandardDTO standardDTO) throws NotFoundException;

    /**
     * Retrieve a list of PartnerStandard transported into PartnerStandardDto
     *
     * @return List of PartnerStandardDTO
     */
    List<PartnerStandardDTO> getAllPartnerStandards();


    /**
     * Update all the PartnerStandard information
     *
     * @param id          Identifier of the PartnerStandardDTO
     * @param standardDTO PartnerStandardDTO object
     */
    void updatePartnerStandard(UUID id, PartnerStandardDTO standardDTO);

    /**
     * Delete a PartnerStandardDTO
     *
     * @param id Identifier of the PartnerStandardDTO
     */

    void deletePartnerStandard(UUID id);

    /**
     * Retrieve a PartnerStandardDto information.
     *
     * @param id Identifier of the PartnerStandardDto
     * @return PartnerStandardDto information for the PartnerStandardDto
     * @throws NotFoundException not found object
     */
    PartnerStandardDTO getPartnerStandardFromId(UUID id) throws NotFoundException;

    /**
     * Get PartnerStandardDto from PartnerActions id and PartnerMeta id VersionStandard
     *
     * @param partnerMetaId      Partner ID
     * @param partnerActionsId   Action ID
     * @param partnerActionsName Action Name
     * @param versionStandard    Standard version
     * @param versionDatamapping Datamapping version
     * @return PartnerStandardDto
     * @throws NotFoundException not found
     */
    List<PartnerStandardDTO> getByCriteria(UUID partnerMetaId, UUID partnerActionsId, String partnerActionsName, String versionStandard, String versionDatamapping, Boolean isActive) throws NotFoundException;
}
