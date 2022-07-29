package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.BadRequest;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Bad request Exception
 */
@Data
public class BadRequestException extends RuntimeException implements Serializable {
    private final BadRequest badRequest;

    public BadRequestException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.badRequest = new BadRequest(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public BadRequestException(String message) {
        super(message);
        this.badRequest = new BadRequest(new GenericError(message));
    }
}
