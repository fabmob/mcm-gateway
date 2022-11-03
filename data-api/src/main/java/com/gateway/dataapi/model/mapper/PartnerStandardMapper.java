package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.database.model.PartnerStandard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PartnerStandardMapper {

    @Mapping(target = "partnerId", source = "id.partner.partnerId")
    @Mapping(target = "partnerActionsId", source = "id.action.partnerActionId")
    @Mapping(target = "versionStandard", source = "id.versionStandard")
    @Mapping(target = "versionDataMapping", source = "id.versionDataMapping")
    @Mapping(target = "adaptersId", source = "adapter.adapterId")
    PartnerStandardDTO mapEntityToDto(PartnerStandard source);

    List<PartnerStandardDTO> mapEntityToDto(List<PartnerStandard> source);

    List<PartnerStandard> mapDtoToEntity(List<PartnerStandardDTO> source);

    @Mapping(target = "id.partner.partnerId", source = "partnerId")
    @Mapping(target = "id.action.partnerActionId", source = "partnerActionsId")
    @Mapping(target = "id.versionStandard", source = "versionStandard")
    @Mapping(target = "id.versionDataMapping", source = "versionDataMapping")
    @Mapping(target = "adapter.adapterId", source = "adaptersId")
    PartnerStandard mapDtoToEntity(PartnerStandardDTO source);
}
