package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.DataMapperMapper;
import com.gateway.dataapi.service.DataMapperService;
import com.gateway.database.model.DataMapper;
import com.gateway.database.service.DataMapperDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DataMapperServiceImp implements DataMapperService {

    @Autowired
    private DataMapperDatabaseService dataMapperService;

    private final DataMapperMapper mapper = Mappers.getMapper(DataMapperMapper.class);

    /**
     * Add a new DataMapperDto
     *
     * @param mapperDTO DataMapperDTO object
     * @return DataMapperDTO information for the DataMapperDTO added
     */
    @Override
    public DataMapperDTO addDataMapper(DataMapperDTO mapperDTO) {
        return mapper.mapEntityToDto(dataMapperService.addDataMapper(mapper.mapDtoToEntity(mapperDTO)));
    }

    /**
     * Retrieve a list of DataMapper transported into DataMapperDto
     *
     * @return List of DataMapperDTO
     */
    @Override
    public List<DataMapperDTO> getAllDataMappers() {
        List<DataMapper> mappers = dataMapperService.getAllDataMappers();
        return mapper.mapEntityToDto(mappers);
    }

    /**
     * Update all the DataMapper information
     *
     * @param id        Identifier of the DataMapperDTO
     * @param mapperDTO DataMapperDTO object
     * @return DataMapperDTO information for the DataMapperDTO put
     */

    @Override
    public DataMapperDTO updateDataMapper(UUID id, DataMapperDTO mapperDTO) {
        DataMapper saveMapper = dataMapperService.updateDataMapper(id, mapper.mapDtoToEntity(mapperDTO));
        return mapper.mapEntityToDto(saveMapper);
    }

    /**
     * Delete a DataMapperDTO
     *
     * @param id Identifier of the DataMapperDTO
     */
    @Override
    public void deleteDataMapper(UUID id) {
        dataMapperService.deleteDataMapper(id);
    }

    /**
     * Retrieve a DataMapperDto information.
     *
     * @param id Identifier of the DataMapperDto
     * @return DataMapperDto information for the DataMapperDto
     * @throws NotFoundException not found object
     */
    @Override
    public DataMapperDTO getDataMapperFromId(UUID id) throws NotFoundException {
        DataMapper datamapper = dataMapperService.findDataMapperById(id);
        return mapper.mapEntityToDto(datamapper);
    }

    /**
     * Get DataMapper from PartnerActions id
     *
     * @param id Identifier of the PartnerActionsDto
     * @return DataMapperDto
     * @throws NotFoundException PartnerAction Not Found
     */
    @Override
    public List<DataMapperDTO> getByPartnerActionId(UUID id) throws NotFoundException {
        List<DataMapper> dataMappers = dataMapperService.findByActionPartnerActionId(id);
        return mapper.mapEntityToDto(dataMappers);
    }

}
