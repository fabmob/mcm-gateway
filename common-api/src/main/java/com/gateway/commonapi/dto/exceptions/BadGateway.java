package com.gateway.commonapi.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadGateway extends GenericError implements Serializable {

    private static final long serialVersionUID = 1905122141950251207L;

    public BadGateway() {
        super();
        this.status = HttpStatus.BAD_GATEWAY.value();
    }

    public BadGateway(String label, String description) {
        super(HttpStatus.BAD_GATEWAY, HttpStatus.BAD_GATEWAY.value(), label, description);
    }

    public BadGateway(int errorCode, String label, String description) {
        super(HttpStatus.BAD_GATEWAY, errorCode, label, description);
    }

    public BadGateway(String description) {
        super(description);
        this.status = HttpStatus.BAD_GATEWAY.value();
    }

    public BadGateway(GenericError genericError) {
        this.setStatus(HttpStatus.BAD_GATEWAY.value());
        this.setErrorCode(genericError.getErrorCode());
        this.setDescription(genericError.getDescription());
        this.setLabel(genericError.getLabel());
        this.setCallId(genericError.getCallId());
        this.setErrorCode(genericError.getErrorCode());
        this.setTimestamp(genericError.getTimestamp());
    }

    @Schema(example = "502")
    public Integer getStatus() {
        return this.status;
    }

}
