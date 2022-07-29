package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequest extends GenericError {

    private static final long serialVersionUID = 1905102001950251207L;

    public BadRequest() {
        super();
        this.status = HttpStatus.BAD_REQUEST.value();
    }

    public BadRequest(String label, String description) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), label, description);
    }

    public BadRequest(int errorCode, String label, String description) {
        super(HttpStatus.BAD_REQUEST, errorCode, label, description);
    }

    public BadRequest(String description) {
        super(description);
        this.status = HttpStatus.BAD_REQUEST.value();
    }

    public BadRequest(GenericError genericError) {
        this.setStatus(HttpStatus.BAD_REQUEST.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "400")
    public Integer getStatus() {
        return this.status;
    }
}
