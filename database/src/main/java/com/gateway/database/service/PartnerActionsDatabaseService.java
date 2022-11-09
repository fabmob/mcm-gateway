package com.gateway.database.service;

import com.gateway.database.model.PartnerActions;

import java.util.List;
import java.util.UUID;

public interface PartnerActionsDatabaseService {
    /**
     * Add a new PartnerActions
     *
     * @param action PartnerActions object
     * @return PartnerActions information for the PartnerActions added
     */
    public PartnerActions addPartnerAction(PartnerActions action);

    /**
     * Retrieve a list of PartnerActions transported into PartnerActions
     *
     * @return List of PartnerActions
     */

    public List<PartnerActions> getAllPartnerActions();

    /**
     * Update all the PartnerActions information
     *
     * @param id     Identifier of the PartnerActions
     * @param action PartnerActions object
     * @return PartnerActions information for the PartnerActions put
     */
    public PartnerActions updatePartnerAction(UUID id, PartnerActions action);

    /**
     * Delete a PartnerActions
     *
     * @param id Identifier of the PartnerActions
     */
    public void deletePartnerAction(UUID id);

    /**
     * Retrieve a PartnerActions information.
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerActions information for the PartnerActions
     */
    public PartnerActions findPartnerActionById(UUID id);


    /**
     * Retrieve a PartnerActions information.
     *
     * @param id Identifier of the PartnerMeta
     * @return PartnerActions information for the PartnerActions
     */
    public List<PartnerActions> findByPartnerMetaId(UUID id);


}
