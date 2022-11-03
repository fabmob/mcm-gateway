package com.gateway.commonapi.exception.config;


import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.gateway.commonapi.constants.ErrorCodeDict.PARTNER_TYPE_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;


@Component
public class PartnerTypeStringToEnumConverter implements Converter<String, PartnerTypeEnum> {


    @Override
    public PartnerTypeEnum convert(String source) {
        try {
            return PartnerTypeEnum.valueOf(source);
        } catch (IllegalArgumentException e) {
            GenericError error = new GenericError();
            error.setErrorCode(PARTNER_TYPE_CODE);
            error.setLabel(UNKNOWN_PARTNER_TYPE);
            error.setDescription(CommonUtils.placeholderFormat(UNKNOWN_PARTNER_TYPE_MESSAGE, PLACEHOLDER, source));
            throw new BadRequestException(error);
        }
    }
}
