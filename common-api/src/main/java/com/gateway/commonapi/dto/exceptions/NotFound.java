package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFound extends GenericError {

    public NotFound() {
        super();
        this.status = HttpStatus.NOT_FOUND.value();
    }

    public NotFound(String label, String description) {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), label, description);
    }

    public NotFound(int errorCode, String label, String description) {
        super(HttpStatus.NOT_FOUND, errorCode, label, description);
    }

    public NotFound(GenericError genericError) {
        this.setStatus(HttpStatus.NOT_FOUND.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "404")
    public Integer getStatus() {
        return this.status;
    }
}
