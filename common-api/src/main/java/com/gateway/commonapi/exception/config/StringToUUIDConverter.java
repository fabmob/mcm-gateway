package com.gateway.commonapi.exception.config;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CommonUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.gateway.commonapi.constants.ErrorCodeDict.INVALID_PARTNER_UUID_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;

@Component
public class StringToUUIDConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String source) {
        try {
            return UUID.fromString(source);
        } catch (IllegalArgumentException e) {
            GenericError error = new GenericError();
            error.setErrorCode(INVALID_PARTNER_UUID_CODE);
            error.setLabel(INVALID_PARTNER_ID);
            error.setDescription(CommonUtils.placeholderFormat(INVALID_PARTNER_ID_MESSAGE, PLACEHOLDER, source));
            throw new BadRequestException(error);
        }
    }


}
