package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * NotFoundException exception
 */
@Data
public class NotFoundException extends RuntimeException implements Serializable {
    private final NotFound notFound;

    public NotFoundException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.notFound = new NotFound(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public NotFoundException(String message) {
        super(message);
        this.notFound = new NotFound(new GenericError(message));
    }
}
