package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class Unauthorized extends GenericError {

    public Unauthorized() {
        super();
        this.status = HttpStatus.UNAUTHORIZED.value();
    }

    public Unauthorized(String label, String description) {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), label, description);
    }

    public Unauthorized(int errorCode, String label, String description) {
        super(HttpStatus.UNAUTHORIZED, errorCode, label, description);
    }

    public Unauthorized(GenericError genericError) {
        this.setStatus(HttpStatus.UNAUTHORIZED.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "401")
    public Integer getStatus() {
        return this.status;
    }
}
