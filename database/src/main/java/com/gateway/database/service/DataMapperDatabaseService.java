package com.gateway.database.service;

import com.gateway.database.model.DataMapper;

import java.util.List;
import java.util.UUID;

public interface DataMapperDatabaseService {
    /**
     * Add a new DataMapper
     *
     * @param datamapper DataMapper object
     * @return DataMapper information for the DataMapper added
     */

    public DataMapper addDataMapper(DataMapper datamapper);

    /**
     * Retrieve a list of DataMapper transported into DataMapper
     *
     * @return List of DataMapper
     */
    public List<DataMapper> getAllDataMappers();

    /**
     * Update all the DataMapper information
     *
     * @param id         Identifier of the DataMapper
     * @param datamapper DataMapper object
     * @return DataMapper information for the DataMapper put
     */
    public DataMapper updateDataMapper(UUID id, DataMapper datamapper);

    /**
     * Delete a DataMapper
     *
     * @param id Identifier of the DataMapper
     */
    public void deleteDataMapper(UUID id);

    /**
     * Retrieve a DataMapper information.
     *
     * @param id Identifier of the DataMapper
     * @return DataMapper information for the DataMapper
     */
    public DataMapper findDataMapperById(UUID id);

    /**
     * Get DataMapper from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return DataMapper
     */
    public List<DataMapper> findByActionPartnerActionId(UUID id);

}
