package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.BusinessError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * BusinessException Exception
 */
@Data
public class BusinessException extends RuntimeException implements Serializable {
    private final BusinessError businessError;

    public BusinessException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.businessError = new BusinessError(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public BusinessException(String message) {
        super(message);
        this.businessError = new BusinessError(new GenericError(message));
    }
}

