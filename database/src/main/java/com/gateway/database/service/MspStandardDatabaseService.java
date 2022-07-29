package com.gateway.database.service;

import com.gateway.database.model.MspStandard;
import com.gateway.database.model.StandardPK;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MspStandardDatabaseService {
    /**
     * Add a new MspStandard
     *
     * @param standard MspStandard object
     * @return MspStandard information for the MspStandard added
     */
    MspStandard addMspStandard(MspStandard standard);

    /**
     * Update all the MspStandard information
     *
     * @param id       Identifier of the MspStandard
     * @param standard MspStandard object
     * @return MspStandard information for the MspStandard put
     */
    MspStandard updateMspStandard(UUID id, MspStandard standard);

    /**
     * Delete a MspStandard
     *
     * @param id Identifier of the MspStandard
     */
    void deleteMspStandard(UUID id);

    /**
     * Retrieve a MspStandard information.
     *
     * @param id Identifier of the MspStandard
     * @return MspStandard information for the MspStandard
     */
    MspStandard findMspStandardById(UUID id);

    /**
     * Retrieve a list of MspStandard transported into MspStandard
     *
     * @return List of MspStandard
     */

    List<MspStandard> getAllMspStandard();


    Optional<MspStandard> findMspStandardByPK(StandardPK standardPK);

    /**
     * Get MspStandard from MspMeta id And MspActions id VersionStandard VersionDatamapping
     *
     * @param mspId MSP ID
     * @param mspActionsId MSP Action ID
     * @param mspActionsName MSP Action ID
     * @param versionStandard Standard version
     * @param versionDatamapping Datamapping version
     * @param isActive version is currently used or not
     * @return MspStandard
     */
    List<MspStandard> getByCriteria(UUID mspId, UUID mspActionsId, String mspActionsName, String versionStandard, String versionDatamapping, Boolean isActive);

    List<MspStandard> findAllByAdapterId(UUID adapterId);
}
