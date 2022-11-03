package com.gateway.commonapi.exception;

import com.gateway.commonapi.dto.exceptions.BadRequest;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Bad request Exception
 */
@Data
public class BadRequestException extends RuntimeException implements Serializable {
    private  BadRequest badRequest;
    private TompError tompError;

    public BadRequestException(GenericError genericError) {
        super(CommonUtils.stringifyGenericErrorDto(genericError));
        this.badRequest = new BadRequest(genericError.getErrorCode(), genericError.getLabel(), genericError.getDescription());
    }

    public BadRequestException(String message) {
        super(message);
        this.badRequest = new BadRequest(new GenericError(message));
    }

    public BadRequestException(TompError tompError) {
        super(CommonUtils.stringifyTompErrorDto(tompError));
        this.tompError = new TompError(tompError.getErrorcode(), tompError.getType(), tompError.getTitle(), tompError.getDetail());

    }
}
