package com.gateway.commonapi.exception.config;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.ZoneType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.gateway.commonapi.constants.ErrorCodeDict.AREA_TYPE_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;

/**
 * custom converter for handling the exception before it was picked up by Mismatch exception
 */

@Component
public class ZoneTypeStringToEnumConverter implements Converter<String, ZoneType> {

    @Override
    public ZoneType convert(String context) {
        try {
            return ZoneType.valueOf(context);
        } catch (IllegalArgumentException e) {
            GenericError error = new GenericError();
            error.setErrorCode(AREA_TYPE_CODE);
            error.setLabel(UNKNOWN_AREA_TYPE);
            error.setDescription(CommonUtils.placeholderFormat(UNKNOWN_AREA_MESSAGE, PLACEHOLDER, context));
            throw new BadRequestException(error);
        }
    }
}
