package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceUnavailable extends GenericError {

    public ServiceUnavailable() {
        super();
        this.status = HttpStatus.SERVICE_UNAVAILABLE.value();
    }

    public ServiceUnavailable(String label, String description) {
        super(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), label, description);
    }

    public ServiceUnavailable(int errorCode, String label, String description) {
        super(HttpStatus.SERVICE_UNAVAILABLE, errorCode, label, description);
    }

    public ServiceUnavailable(GenericError genericError) {
        this.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "503")
    public Integer getStatus() {
        return this.status;
    }
}
