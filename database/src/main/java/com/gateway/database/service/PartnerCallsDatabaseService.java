package com.gateway.database.service;

import com.gateway.database.model.PartnerCalls;

import java.util.List;
import java.util.UUID;

public interface PartnerCallsDatabaseService {

    /**
     * Add a new PartnerCalls
     *
     * @param call PartnerCalls object
     * @return PartnerCalls information for the PartnerCalls added
     */
    public PartnerCalls addPartnerCall(PartnerCalls call);

    /**
     * Retrieve a list of PartnerCalls transported into PartnerCalls
     *
     * @return List of PartnerCalls
     */

    public List<PartnerCalls> getAllCalls();

    /**
     * Update all the PartnerCalls information
     *
     * @param id   Identifier of the PartnerCalls
     * @param call PartnerCalls object
     * @return PartnerCalls information for the PartnerCalls put
     */
    public PartnerCalls updatePartnerCall(UUID id, PartnerCalls call);

    /**
     * Delete a PartnerCalls
     *
     * @param id Identifier of the PartnerCalls
     */

    public void deletePartnerCall(UUID id);

    /**
     * Retrieve a PartnerCalls information.
     *
     * @param id Identifier of the PartnerCalls
     * @return PartnerCalls information for the PartnerCalls
     */

    public PartnerCalls findPartnerCallsById(UUID id);

    /**
     * Get PartnerCalls from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerCalls
     */
    public List<PartnerCalls> findByActionPartnerActionId(UUID id);

}
