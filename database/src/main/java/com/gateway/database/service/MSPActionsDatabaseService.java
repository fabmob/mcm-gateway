package com.gateway.database.service;

import com.gateway.database.model.MSPActions;

import java.util.List;
import java.util.UUID;

public interface MSPActionsDatabaseService {
    /**
     * Add a new MspActions
     *
     * @param action MspActions object
     * @return MspActions informations for the MspActions added
     */
    public MSPActions addMspAction(MSPActions action);

    /**
     * Retrieve a list of MspActions transported into MspActions
     *
     * @return List of MspActions
     */

    public List<MSPActions> getAllMspActions();

    /**
     * Update all the MspActions informations
     *
     * @param id     Identifier of the MspActions
     * @param action MspActions object
     * @return MspActions informations for the MspActions puted
     */
    public MSPActions updateMspAction(UUID id, MSPActions action);

    /**
     * Delete a MspActions
     *
     * @param id Identifier of the MspActions
     */
    public void deleteMspAction(UUID id);

    /**
     * Retrieve a MspActions informations.
     *
     * @param id Identifier of the MspActions
     * @return MspActions informations for the MspActions
     */
    public MSPActions findMspActionById(UUID id);


    /**
     * Retrieve a MspActions informations.
     *
     * @param id Identifier of the MspMeta
     * @return MspActions informations for the MspActions
     */
    public List<MSPActions> findByMspMetaId(UUID id);


}
