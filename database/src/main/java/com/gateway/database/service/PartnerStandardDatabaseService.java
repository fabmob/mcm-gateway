package com.gateway.database.service;

import com.gateway.database.model.PartnerStandard;
import com.gateway.database.model.StandardPK;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerStandardDatabaseService {
    /**
     * Add a new PartnerStandard
     *
     * @param standard PartnerStandard object
     * @return PartnerStandard information for the PartnerStandard added
     */
    PartnerStandard addPartnerStandard(PartnerStandard standard);

    /**
     * Update all the PartnerStandard information
     *
     * @param id       Identifier of the PartnerStandard
     * @param standard PartnerStandard object
     * @return PartnerStandard information for the PartnerStandard put
     */
    PartnerStandard updatePartnerStandard(UUID id, PartnerStandard standard);

    /**
     * Delete a PartnerStandard
     *
     * @param id Identifier of the PartnerStandard
     */
    void deletePartnerStandard(UUID id);

    /**
     * Retrieve a PartnerStandard information.
     *
     * @param id Identifier of the PartnerStandard
     * @return PartnerStandard information for the PartnerStandard
     */
    PartnerStandard findPartnerStandardById(UUID id);

    /**
     * Retrieve a list of PartnerStandard transported into PartnerStandard
     *
     * @return List of PartnerStandard
     */

    List<PartnerStandard> getAllPartnerStandard();


    Optional<PartnerStandard> findPartnerStandardByPK(StandardPK standardPK);

    /**
     * Get PartnerStandard from PartnerMeta id And PartnerActions id VersionStandard VersionDatamapping
     *
     * @param partnerId          Partner ID
     * @param partnerActionsId   Partner Action ID
     * @param partnerActionsName Partner Action ID
     * @param versionStandard    Standard version
     * @param versionDatamapping Datamapping version
     * @param isActive           version is currently used or not
     * @return PartnerStandard
     */
    List<PartnerStandard> getByCriteria(UUID partnerId, UUID partnerActionsId, String partnerActionsName, String versionStandard, String versionDatamapping, Boolean isActive);

    List<PartnerStandard> findAllByAdapterId(UUID adapterId);
}
