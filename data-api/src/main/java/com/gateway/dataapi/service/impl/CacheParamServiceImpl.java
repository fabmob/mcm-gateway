package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.dataapi.model.mapper.CacheParamMapper;
import com.gateway.dataapi.service.CacheParamService;
import com.gateway.database.model.CacheParam;
import com.gateway.database.service.CacheParamDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CacheParamServiceImpl implements CacheParamService {

    @Autowired
    private CacheParamDatabaseService cacheParamDatabaseService;


    private final CacheParamMapper mapper = Mappers.getMapper(CacheParamMapper.class);

    @Override
    public CacheParamDTO addCacheParam(CacheParamDTO mapperDTO) {
        return mapper.mapEntityToDTO(cacheParamDatabaseService.addCacheParam(mapper.mapDtoToEntity(mapperDTO)));
    }

    @Override
    public List<CacheParamDTO> getAllCacheParams() {
        List<CacheParam> mappers = cacheParamDatabaseService.getAllCacheParams();
        return mapper.mapEntityToDTO(mappers);
    }


    @Override
    public void deleteCacheParamById(UUID cacheParamId) {
        cacheParamDatabaseService.deleteCacheParam(cacheParamId);
    }


    @Override
    public void updateCacheParam(UUID cacheParamId, CacheParamDTO body) {
        CacheParam saveCacheParam = cacheParamDatabaseService.updateCacheParam(cacheParamId, mapper.mapDtoToEntity(body));
        mapper.mapEntityToDTO(saveCacheParam);
    }

    @Override
    public CacheParamDTO getCacheParamFromID(UUID cacheParamId) {
        CacheParam cacheParam = cacheParamDatabaseService.findCacheParamByID(cacheParamId);
        return mapper.mapEntityToDTO(cacheParam);
    }

    @Override
    public List<CacheParamDTO> getAllCacheParamByCriteria(UUID partnerId, String actionType) {
        return mapper.mapEntityToDTO(cacheParamDatabaseService.getAllCacheParamByCriteria(partnerId, actionType));
    }

}
