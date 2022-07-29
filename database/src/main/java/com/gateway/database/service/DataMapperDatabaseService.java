package com.gateway.database.service;

import java.util.List;
import java.util.UUID;

import com.gateway.database.model.DataMapper;

public interface DataMapperDatabaseService {
    /**
     * Add a new DataMapper
     *
     * @param datamapper DataMapper object
     * @return DataMapper informations for the DataMapper added
     */

    public DataMapper addDataMapper(DataMapper datamapper);

    /**
     * Retrieve a list of DataMapper transported into DataMapper
     *
     * @return List of DataMapper
     */
    public List<DataMapper> getAllDataMappers();

    /**
     * Update all the DataMapper informations
     *
     * @param id         Identifier of the DataMapper
     * @param datamapper DataMapper object
     * @return DataMapper informations for the DataMapper puted
     */
    public DataMapper updateDataMapper(UUID id, DataMapper datamapper);

    /**
     * Delete a DataMapper
     *
     * @param id Identifier of the DataMapper
     */
    public void deleteDataMapper(UUID id);

    /**
     * Retrieve a DataMapper informations.
     *
     * @param id Identifier of the DataMapper
     * @return DataMapper informations for the DataMapper
     */
    public DataMapper findDataMapperById(UUID id);

    /**
     * Get DataMapper from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return DataMapper
     */
    public List<DataMapper> findByActionMspActionId(UUID id);

}
