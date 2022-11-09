package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.Conflict;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * ConflictException Exception
 */
@Data
public class ConflictException extends RuntimeException implements Serializable {
    private final Conflict conflict;

    public ConflictException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.conflict = new Conflict(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public ConflictException(String message) {
        super(message);
        this.conflict = new Conflict(new GenericError(message));
    }
}
