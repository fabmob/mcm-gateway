package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface DataMapperService {

    /**
     * Add a new DataMapperDto
     *
     * @param datamapper DataMapperDTO object
     * @return DataMapperDTO informations for the DataMapperDTO added
     */
    DataMapperDTO addDataMapper(DataMapperDTO datamapper);

    /**
     * Retrieve a list of DataMapper transported into DataMapperDto
     *
     * @return List of DataMapperDTO
     */
    List<DataMapperDTO> getAllDataMappers();

    /**
     * Update all the DataMapper informations
     *
     * @param id         Identifier of the DataMapperDTO
     * @param datamapper DataMapperDTO object
     * @return DataMapperDTO informations for the DataMapperDTO puted
     */
    DataMapperDTO updateDataMapper(UUID id, DataMapperDTO datamapper);

    /**
     * Delete a DataMapperDTO
     *
     * @param id Identifier of the DataMapperDTO
     */
    void deleteDataMapper(UUID id);

    /**
     * Retrieve a DataMapperDto informations.
     *
     * @param id Identifier of the DataMapperDto
     * @return DataMapperDto informations for the DataMapperDto
     * @throws NotFoundException not found object
     */
    DataMapperDTO getDataMapperFromId(UUID id) throws NotFoundException;

    /**
     * Get DataMapper from MspActions id
     *
     * @param id Identifier of the MspActionsDto
     * @return DataMapperDto
     * @throws NotFoundException element is not found
     */
    List<DataMapperDTO> getByMspActionId(UUID id) throws NotFoundException;
}
