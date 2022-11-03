package com.gateway.database.service;

import com.gateway.database.model.Adapters;

import java.util.List;
import java.util.UUID;

public interface AdaptersDatabaseService {
    /**
     * Add a new Adapter
     *
     * @param adapter Adapter object
     * @return Adapter information for the Adapter added
     */
    public Adapters addAdapter(Adapters adapter);

    /**
     * Retrieve a list of Adapters transported into Adapters
     *
     * @return List of Adapters
     */

    public List<Adapters> getAllAdapters();


    /**
     * Retrieve a Adapter information.
     *
     * @param id Identifier of the Adapter
     * @return Adapter information for the Adapter
     */

    public Adapters findAdapterById(UUID id);

    /**
     * Delete a Adapter
     *
     * @param id Identifier of the Adapter
     */

    public void deleteAdapter(UUID id);
}
