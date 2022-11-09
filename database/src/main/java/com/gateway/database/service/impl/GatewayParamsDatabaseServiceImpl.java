package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.GatewayParams;
import com.gateway.database.repository.GatewayParamsRepository;
import com.gateway.database.service.GatewayParamsDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Slf4j
@Service
public class GatewayParamsDatabaseServiceImpl implements GatewayParamsDatabaseService {

    @Autowired
    GatewayParamsRepository gatewayParamsRepository;

    @Autowired
    private ErrorMessages errorMessage;

    @Override
    public GatewayParams addGatewayParams(GatewayParams gatewayParams) {
        if (gatewayParams.getParamKey() != null) {
            if (gatewayParamsRepository.findById(gatewayParams.getParamKey()).isPresent()) {
                throw new ConflictException(CommonUtils.placeholderFormat(GATEWAY_PARAMS_WITH_ID_ALREADY_EXISTS, FIRST_PLACEHOLDER, gatewayParams.getParamKey()));
            } else {
                try {
                    return gatewayParamsRepository.save(gatewayParams);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw e;
                }
            }
        } else {
            throw new ConflictException(GATEWAY_PARAMS_WITH_NULL_KEY);
        }

    }

    @Override
    public List<GatewayParams> getAllGatewayParams() {
        return (List<GatewayParams>) gatewayParamsRepository.findAll();
    }

    @Override
    public GatewayParams findGatewayParamsByParamKey(String paramKey) {
        return gatewayParamsRepository.findById(paramKey)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GATEWAY_PARAMS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, paramKey))));


    }

    @Override
    public GatewayParams updateGatewayParams(String paramKey, GatewayParams gatewayParams) {
        try {

            this.findGatewayParamsByParamKey(paramKey);
            gatewayParams.setParamKey(paramKey);
            return gatewayParamsRepository.save(gatewayParams);

        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GATEWAY_PARAMS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, paramKey)));
        }
    }

    @Override
    public void deleteGatewayParams(String paramKey) {
        try {
            gatewayParamsRepository.deleteById(paramKey);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GATEWAY_PARAMS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, paramKey)));

        }
    }
}
