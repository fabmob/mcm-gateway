package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.Unauthorized;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Unauthorized Exception
 */
@Data
public class UnauthorizedException extends RuntimeException implements Serializable {
    private final Unauthorized unauthorized;

    public UnauthorizedException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.unauthorized = new Unauthorized(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public UnauthorizedException(String message) {
        super(message);
        this.unauthorized = new Unauthorized(new GenericError(message));
    }
}
