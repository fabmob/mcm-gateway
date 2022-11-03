package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * ConflictException Exception
 */
@Data
public class InternalException extends RuntimeException implements Serializable {
    private final GenericError internalError;

    public InternalException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.internalError = genericError;
    }

    public InternalException(String message) {
        super(message);
        this.internalError = new GenericError(message);
    }
}
