package com.gateway.database.service;

import com.gateway.database.model.MSPCalls;

import java.util.List;
import java.util.UUID;

public interface MSPCallsDatabaseService {

    /**
     * Add a new MspCalls
     *
     * @param call MspCalls object
     * @return MspCalls informations for the MspCalls added
     */
    public MSPCalls addMspCall(MSPCalls call);

    /**
     * Retrieve a list of MspCalls transported into MspCalls
     *
     * @return List of MspCalls
     */

    public List<MSPCalls> getAllCalls();

    /**
     * Update all the MspCalls informations
     *
     * @param id   Identifier of the MspCalls
     * @param call MspCalls object
     * @return MspCalls informations for the MspCalls puted
     */
    public MSPCalls updateMspCall(UUID id, MSPCalls call);

    /**
     * Delete a MspCalls
     *
     * @param id Identifier of the MspCalls
     */

    public void deleteMspCall(UUID id);

    /**
     * Retrieve a MspCalls informations.
     *
     * @param id Identifier of the MspCalls
     * @return MspCalls informations for the MspCalls
     */

    public MSPCalls findMspCallsById(UUID id);

    /**
     * Get MspCalls from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return MspCalls
     */
    public List<MSPCalls> findByActionMspActionId(UUID id);

}
