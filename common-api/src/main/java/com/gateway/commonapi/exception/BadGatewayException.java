package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.BadGateway;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Bad gateway Exception
 */
@Data
public class BadGatewayException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1005102001950251207L;

    private final BadGateway badGatewayError;

    public BadGatewayException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        if (genericError != null) {
            this.badGatewayError = new BadGateway(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
        } else {
            this.badGatewayError = new BadGateway();
        }
    }

    public BadGatewayException(String message) {
        super(message);
        this.badGatewayError = new BadGateway(new GenericError(message));
    }
}
