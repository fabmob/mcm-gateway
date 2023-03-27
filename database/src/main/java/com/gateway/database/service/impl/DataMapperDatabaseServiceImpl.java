package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.DataMapper;
import com.gateway.database.repository.DataMapperRepository;
import com.gateway.database.service.DataMapperDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.commonapi.constants.GlobalConstants.INTERNAL_FIELD;
import static com.gateway.commonapi.constants.GlobalConstants.UNRECOGNISED_FUNCTION;
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
     * @return DataMapper information for the DataMapper added
     */

    @Override
    public DataMapper addDataMapper(DataMapper datamapper) {
        DataMapper mapper;
        if (datamapper.getAction() != null && StringUtils.isNotBlank(datamapper.getInternalField())) {
            Optional<List<DataMapper>> existingDataMappers = dataMapperRepository.findByActionPartnerActionIdAndInternalField(datamapper.getAction().getPartnerActionId(), datamapper.getInternalField());
            if (existingDataMappers.isPresent() && !CollectionUtils.isEmpty(existingDataMappers.get())) {
                throw new ConflictException(CommonUtils.placeholderFormat(DATA_MAPPER_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD, FIRST_PLACEHOLDER, datamapper.getAction().getPartnerActionId().toString(), SECOND_PLACEHOLDER, datamapper.getInternalField()));
            }
        }
        String format = datamapper.getFormat();
        if (StringUtils.isNotBlank(format) && !CommonUtils.isValidFunction(format)) {
            throw new InternalException(CommonUtils.placeholderFormat(UNRECOGNISED_FUNCTION, INTERNAL_FIELD, StringUtils.isNotBlank(datamapper.getInternalField()) ? datamapper.getInternalField() : "null"));
        }

        try {
            mapper = dataMapperRepository.save(datamapper);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER,
                    (datamapper.getAction() != null ? datamapper.getAction().getPartnerActionId().toString() : StringUtils.EMPTY))));
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
        String format = datamapper.getFormat();
        if (StringUtils.isNotBlank(format) && !CommonUtils.isValidFunction(format)) {
            throw new InternalException(CommonUtils.placeholderFormat(UNRECOGNISED_FUNCTION, INTERNAL_FIELD, StringUtils.isNotBlank(datamapper.getInternalField()) ? datamapper.getInternalField() : "null"));
        }
        try {
            dataMapperRepository.save(datamapper);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, datamapper.getAction().getPartnerActionId().toString()));
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
     * Get DataMapper from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return DataMapper
     */
    @Override
    public List<DataMapper> findByActionPartnerActionId(UUID id) {
        List<DataMapper> datamappers = dataMapperRepository.findByActionPartnerActionId(id);
        if (datamappers == null || datamappers.isEmpty()) {
           log.info(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(DATA_MAPPER_PARTNER_ACTIONS_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return datamappers;
    }

}