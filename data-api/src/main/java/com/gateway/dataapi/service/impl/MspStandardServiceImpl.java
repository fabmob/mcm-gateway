package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.MspStandardMapper;
import com.gateway.dataapi.service.MspStandardService;
import com.gateway.database.model.MspStandard;
import com.gateway.database.service.MspStandardDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MspStandardServiceImpl implements MspStandardService {


    @Autowired
    private MspStandardDatabaseService mspStandardDatabaseService;

    private final MspStandardMapper mapper = Mappers.getMapper(MspStandardMapper.class);


    @Override
    public MspStandardDTO addMspStandard(MspStandardDTO standardDTO) throws NotFoundException {
        return mapper.mapEntityToDto(mspStandardDatabaseService.addMspStandard(mapper.mapDtoToEntity(standardDTO)));
    }

    @Override
    public MspStandardDTO updateMspStandard(UUID id, MspStandardDTO standardDTO) {
        MspStandard saveStandard = mspStandardDatabaseService.updateMspStandard(id, mapper.mapDtoToEntity(standardDTO));
        return mapper.mapEntityToDto(saveStandard);
    }

    @Override
    public void deleteMspStandard(UUID id) {
        mspStandardDatabaseService.deleteMspStandard(id);
    }

    /**
     * Retrieve a MspStandardDto informations.
     *
     * @param id Identifier of the MspStandardDto
     * @return MspStandardDto informations for the MspStandardDto
     * @throws NotFoundException not found object
     */
    @Override
    public MspStandardDTO getMspStandardFromId(UUID id) throws NotFoundException {
        return mapper.mapEntityToDto(mspStandardDatabaseService.findMspStandardById(id));
    }


    /**
     * Get MspStandardDto from MspActions id and MspMeta id VersionStandard VersionStandard
     *
     * @param mspMetaId
     * @param mspActionsId
     * @param versionStandard
     * @param versionDatamapping
     * @return MspStandardDto
     * @throws NotFoundException
     */
    @Override
    public List<MspStandardDTO> getByCriteria(UUID mspMetaId, UUID mspActionsId, String versionStandard, String versionDatamapping) throws NotFoundException {
        return mapper.mapEntityToDto(mspStandardDatabaseService.getByCriteria(mspMetaId, mspActionsId, versionStandard, versionDatamapping));
    }

    @Override
    public List<MspStandardDTO> getAllMspStandards() {
        List<MspStandard> Standards = mspStandardDatabaseService.getAllMspStandard();
        return mapper.mapEntityToDto(Standards);
    }


}