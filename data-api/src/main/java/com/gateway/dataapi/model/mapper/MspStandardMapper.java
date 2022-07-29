package com.gateway.dataapi.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.database.model.MspStandard;

@Mapper
public interface MspStandardMapper {

    @Mapping(target = "mspMetaId", source = "id.msp.mspId")
    @Mapping(target = "mspActionsId", source = "id.action.mspActionId")
    @Mapping(target = "versionStandard", source = "id.versionStandard")
    @Mapping(target = "versionDataMapping", source = "id.versionDataMapping")

    MspStandardDTO mapEntityToDto(MspStandard source);

    List<MspStandardDTO> mapEntityToDto(List<MspStandard> source);

    List<MspStandard> mapDtoToEntity(List<MspStandardDTO> source);

    @Mapping(target = "id.msp.mspId", source = "mspMetaId")
    @Mapping(target = "id.action.mspActionId", source = "mspActionsId")
    @Mapping(target = "id.versionStandard", source = "versionStandard")
    @Mapping(target = "id.versionDataMapping", source = "versionDataMapping")
    MspStandard mapDtoToEntity(MspStandardDTO source);
}