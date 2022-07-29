package com.gateway.dataapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.MspStandardMapper;
import com.gateway.dataapi.service.MspStandardService;
import com.gateway.database.model.MspStandard;
import com.gateway.database.service.MspStandardDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gateway.dataapi.util.constant.DataApiMessageDict.*;

@Service
@Slf4j
public class MspStandardServiceImpl implements MspStandardService {



    @Autowired
    private MspStandardDatabaseService mspStandardDatabaseService;

    private final MspStandardMapper mapper = Mappers.getMapper(MspStandardMapper.class);


    @Override
    public MspStandardDTO addMspStandard(MspStandardDTO standardDTO) throws NotFoundException {
        checkData(standardDTO);
        return mapper.mapEntityToDto(mspStandardDatabaseService.addMspStandard(mapper.mapDtoToEntity(standardDTO)));
    }

    @Override
    public void updateMspStandard(UUID id, MspStandardDTO standardDTO) {
        this.checkData(standardDTO);
        if(log.isDebugEnabled()){
            try{
                log.debug(new ObjectMapper().writeValueAsString(standardDTO));
            }catch (Exception e){
                throw new UnsupportedOperationException(UNABLE_TO_CONVERT_OBJECT_TO_JSON);
            }
        }
        mspStandardDatabaseService.updateMspStandard(id, mapper.mapDtoToEntity(standardDTO));

    }

    @Override
    public void deleteMspStandard(UUID id) {
        mspStandardDatabaseService.deleteMspStandard(id);
    }

    /**
     * Retrieve a MspStandardDto information.
     *
     * @param id Identifier of the MspStandardDto
     * @return MspStandardDto information for the MspStandardDto
     * @throws NotFoundException not found object
     */
    @Override
    public MspStandardDTO getMspStandardFromId(UUID id) throws NotFoundException {
        return mapper.mapEntityToDto(mspStandardDatabaseService.findMspStandardById(id));
    }


    /**
     * Get MspStandardDto from MspActions id and MspMeta id VersionStandard
     *
     * @param mspMetaId MSP ID
     * @param mspActionsName Action Name
     * @param versionStandard Standard version
     * @param versionDatamapping Datamapping version
     * @return MspStandardDto
     * @throws NotFoundException not found
     */
    @Override
    public List<MspStandardDTO> getByCriteria(UUID mspMetaId,UUID mspActionsId, String mspActionsName, String versionStandard, String versionDatamapping,Boolean isActive) throws NotFoundException {
        return mapper.mapEntityToDto(mspStandardDatabaseService.getByCriteria(mspMetaId,mspActionsId, mspActionsName, versionStandard, versionDatamapping, isActive));
    }

    @Override
    public List<MspStandardDTO> getAllMspStandards() {
        List<MspStandard> standards = mspStandardDatabaseService.getAllMspStandard();
        return mapper.mapEntityToDto(standards);
    }

    /**
     * check data the PK if not null
     * @param mspStandardDTO body of the request
     */
    private void checkData(MspStandardDTO mspStandardDTO){
        if (mspStandardDTO.getMspActionsId() == null){
            throw new BadRequestException(ACTION_ID_IS_NULL);
        }
        if (mspStandardDTO.getMspId() == null){
            throw new BadRequestException(MSP_ID_IS_NULL);
        }
        if (mspStandardDTO.getAdaptersId() == null){
            throw new BadRequestException(ADAPTER_ID_IS_NULL);
        }
        if (mspStandardDTO.getVersionDataMapping() == null){
            throw new BadRequestException(VERSION_DATA_MAPPING_IS_NULL);
        }
        if (mspStandardDTO.getVersionStandard() == null){
            throw new BadRequestException(VERSION_STANDARD_IS_NULL);
        }
    }


}