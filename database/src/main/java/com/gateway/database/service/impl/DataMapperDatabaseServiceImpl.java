package com.gateway.database.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.database.model.DataMapper;
import com.gateway.database.repository.DataMapperRepository;
import com.gateway.database.service.DataMapperDatabaseService;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Slf4j
@Service
public class DataMapperDatabaseServiceImpl implements DataMapperDatabaseService {

    @Autowired
    private DataMapperRepository dataMapperRepository;

    @Autowired
    private ErrorMessages errorMessage;

    public DataMapperDatabaseServiceImpl(DataMapperRepository dataMapperRepo) {
        super();
        this.dataMapperRepository = dataMapperRepo;
    }

    public DataMapperDatabaseServiceImpl() {
    }

    public DataMapperRepository getDataMapperRepository() {
        return dataMapperRepository;
    }

    /**
     * Add a new DataMapper
     *
     * @param datamapper DataMapper object
     * @return DataMapper informations for the DataMapper added
     */

    @Override
    public DataMapper addDataMapper(DataMapper datamapper) {
        DataMapper mapper;
        if(datamapper.getAction()!= null && StringUtils.isNotBlank(datamapper.getChampInterne())){
            if (dataMapperRepository.findByActionMspActionIdAndChampInterne(datamapper.getAction().getMspActionId(), datamapper.getChampInterne()).isPresent()) {
                throw new ConflictException(CommonUtils.placeholderFormat(DATA_MAPPER_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD, FIRST_PLACEHOLDER, datamapper.getAction().getMspActionId().toString(), SECOND_PLACEHOLDER, datamapper.getChampInterne()));
            }
        }

        try {
            mapper = dataMapperRepository.save(datamapper);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER,
                    ( datamapper.getAction() != null ? datamapper.getAction().getMspActionId().toString() : StringUtils.EMPTY))));
        }
        return mapper;
    }

    /**
     * Retrieve a list of DataMapper transported into DataMapper
     *
     * @return List of DataMapper
     */
    @Override
    public List<DataMapper> getAllDataMappers() {
        return (List<DataMapper>) dataMapperRepository.findAll();
    }

    /**
     * Update all the DataMapper information
     *
     * @param id         Identifier of the DataMapper
     * @param datamapper DataMapper object
     * @return DataMapper information for the DataMapper put
     */
    @Override
    public DataMapper updateDataMapper(UUID id, DataMapper datamapper) {
        datamapper.setDataMapperId(id);
        try {
            dataMapperRepository.save(datamapper);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, datamapper.getAction().getMspActionId().toString()));
        }
        return datamapper;
    }

    /**
     * Delete a DataMapper
     *
     * @param id Identifier of the DataMapper
     */
    @Override
    public void deleteDataMapper(UUID id) {
        try {
            dataMapperRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(DATA_MAPPER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()));
        }

    }

    /**
     * Retrieve a DataMapper information.
     *
     * @param id Identifier of the DataMapper
     * @return DataMapper information for the DataMapper
     */

    @Override
    public DataMapper findDataMapperById(UUID id) {
        return dataMapperRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(DATA_MAPPER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));
    }

    /**
     * Get DataMapper from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return DataMapper
     */
    @Override
    public List<DataMapper> findByActionMspActionId(UUID id) {
        List<DataMapper> datamappers = dataMapperRepository.findByActionMspActionId(id);
        if (datamappers == null || datamappers.isEmpty()) {
            throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(DATA_MAPPER_MSP_ACTIONS_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return datamappers;
    }

}