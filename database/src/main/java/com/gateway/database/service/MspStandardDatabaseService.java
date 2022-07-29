package com.gateway.database.service;

import com.gateway.database.model.MspStandard;

import java.util.List;
import java.util.UUID;

public interface MspStandardDatabaseService {
    /**
     * Add a new MspStandard
     *
     * @param standard MspStandard object
     * @return MspStandard informations for the MspStandard added
     */
    public MspStandard addMspStandard(MspStandard standard);

    /**
     * Update all the MspStandard informations
     *
     * @param id       Identifier of the MspStandard
     * @param standard MspStandard object
     * @return MspStandard informations for the MspStandard puted
     */
    public MspStandard updateMspStandard(UUID id, MspStandard standard);

    /**
     * Delete a MspStandard
     *
     * @param id Identifier of the MspStandard
     */
    public void deleteMspStandard(UUID id);

    /**
     * Retrieve a MspStandard informations.
     *
     * @param id Identifier of the MspStandard
     * @return MspStandard informations for the MspStandard
     */
    public MspStandard findMspStandardById(UUID id);

    /**
     * Retrieve a list of MspStandard transported into MspStandard
     *
     * @return List of MspStandard
     */

    public List<MspStandard> getAllMspStandard();


    /**
     * Get MspStandard from MspMeta id And MspActions id VersionStandard VersionDatamapping
     *
     * @param mspMetaId
     * @param mspActionsId
     * @param versionStandard
     * @param versionDatamapping
     * @return MspStandard
     */
    public List<MspStandard> getByCriteria(UUID mspMetaId, UUID mspActionsId, String versionStandard, String versionDatamapping);
}
