package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.database.model.CacheParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CacheParamMapper {
    @Mapping(target = "partnerId", source = "cacheParamPK.partner.partnerId")
    @Mapping(target = "actionType", source = "cacheParamPK.actionType")
    CacheParamDTO mapEntityToDTO(CacheParam source);

    List<CacheParamDTO> mapEntityToDTO(List<CacheParam> source);

    List<CacheParam> mapDtoToEntity(List<CacheParamDTO> source);

    @Mapping(target = "cacheParamPK.partner.partnerId", source = "partnerId")
    @Mapping(target = "cacheParamPK.actionType", source = "actionType")
    CacheParam mapDtoToEntity(CacheParamDTO source);
}
