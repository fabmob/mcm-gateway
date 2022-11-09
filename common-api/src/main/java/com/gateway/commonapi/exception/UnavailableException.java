package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.ServiceUnavailable;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Unavailable exception
 */
@Data
public class UnavailableException extends RuntimeException implements Serializable {
    private final ServiceUnavailable serviceUnavailable;

    public UnavailableException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.serviceUnavailable = new ServiceUnavailable(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public UnavailableException(String message) {
        super(message);
        this.serviceUnavailable = new ServiceUnavailable(new GenericError(message));
    }
}
