package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.cache.CacheUtil;
import com.gateway.commonapi.cache.GatewayParamStatusManager;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.dataapi.model.mapper.GatewayParamsMapper;
import com.gateway.dataapi.service.GatewayParamsService;
import com.gateway.database.model.GatewayParams;
import com.gateway.database.service.GatewayParamsDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;
import static com.gateway.database.util.constant.DataMessageDict.VALID_CACHE_ACTIVATION_VALUES;

@Service
public class GatewayParamsServiceImpl implements GatewayParamsService {

    @Autowired
    private GatewayParamsDatabaseService gatewayParamsDatabaseService;

    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private GatewayParamStatusManager gatewayParamStatusManager;

    private final GatewayParamsMapper mapper = Mappers.getMapper(GatewayParamsMapper.class);


    @Override
    public GatewayParamsDTO addGatewayParamsDTO(GatewayParamsDTO gatewayParamsDTO) {
        if (Objects.equals(gatewayParamsDTO.getParamKey(), CACHE_ACTIVATION) && !Objects.equals(gatewayParamsDTO.getParamValue(), Boolean.TRUE.toString()) && !Objects.equals(gatewayParamsDTO.getParamValue(), Boolean.FALSE.toString())) {
            throw new BadRequestException(VALID_CACHE_ACTIVATION_VALUES);
        }
        GatewayParamsDTO gatewayParamsDTOAfterSave = mapper.mapEntityToDto(gatewayParamsDatabaseService.addGatewayParams(mapper.mapDtoToEntity(gatewayParamsDTO)));
        if (Objects.equals(gatewayParamsDTO.getParamKey(), CACHE_ACTIVATION)) {
            gatewayParamStatusManager.synchronizeCacheStatus();
        }
        return gatewayParamsDTOAfterSave;
    }

    @Override
    public List<GatewayParamsDTO> getAllGatewayParamsDTO() {
        List<GatewayParams> gatewayParams = gatewayParamsDatabaseService.getAllGatewayParams();
        return mapper.mapEntityToDto(gatewayParams);
    }

    @Override
    public GatewayParamsDTO findGatewayParamsDTOByParamKey(String paramKey) {
        GatewayParams gatewayParam = gatewayParamsDatabaseService.findGatewayParamsByParamKey(paramKey);
        return mapper.mapEntityToDto(gatewayParam);
    }

    @Override
    public GatewayParamsDTO updateGatewayParamsDTO(String paramKey, GatewayParamsDTO gatewayParamsDTO) {

        GatewayParams gatewayParam = gatewayParamsDatabaseService.updateGatewayParams(paramKey, mapper.mapDtoToEntity(gatewayParamsDTO));
        if (Objects.equals(gatewayParamsDTO.getParamKey(), CACHE_ACTIVATION)) {
            gatewayParamStatusManager.synchronizeCacheStatus();
        }
        return mapper.mapEntityToDto(gatewayParam);
    }

    @Override
    public void deleteGatewayParamsDTO(String paramKey) {
        gatewayParamsDatabaseService.deleteGatewayParams(paramKey);
    }
}
