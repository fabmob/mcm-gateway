package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessError extends GenericError {

    public BusinessError() {
        super();
        this.status = HttpStatus.UNPROCESSABLE_ENTITY.value();
    }

    public BusinessError(String label, String description) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.value(), label, description);
    }

    public BusinessError(int errorCode, String label, String description) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode, label, description);
    }

    public BusinessError(String description) {
        super(description);
        this.status = HttpStatus.UNPROCESSABLE_ENTITY.value();
    }

    public BusinessError(GenericError genericError) {
        this.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "422")
    @Override
    public Integer getStatus() {
        return this.status;
    }
}
