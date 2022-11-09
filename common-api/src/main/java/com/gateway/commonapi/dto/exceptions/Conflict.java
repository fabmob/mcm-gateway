package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class Conflict extends GenericError {

    public Conflict() {
        super();
        this.status = HttpStatus.CONFLICT.value();
    }

    public Conflict(String label, String description) {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), label, description);
    }

    public Conflict(int errorCode, String label, String description) {
        super(HttpStatus.CONFLICT, errorCode, label, description);
    }

    public Conflict(GenericError genericError) {
        this.setStatus(HttpStatus.CONFLICT.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "409")
    @Override
    public Integer getStatus() {
        return this.status;
    }
}
