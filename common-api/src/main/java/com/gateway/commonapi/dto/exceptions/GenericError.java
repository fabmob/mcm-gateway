package com.gateway.commonapi.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Schema(name = "GenericError",
        description = "Generic error response for all kind of errors")
@Data
@AllArgsConstructor
public class GenericError {

    @Schema(name = "status",
            description = "Status of the http status error",
            type = "integer",
            example = "500",
            nullable = false,
            required = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status")
    protected Integer status;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GlobalConstants.GATEWAY_DATE_FORMAT);
    @Schema(name = "errorCode",
            description = "Functional error code, -1 if technical error",
            type = "integer",
            example = "12",
            nullable = false,
            required = true)
    @JsonProperty("errorCode")
    private Integer errorCode;
    @Schema(name = "label", description = "label of the error", type = "integer", example = "Msp not activated yet", nullable = true, required = true)
    @JsonProperty("label")
    private String label;
    @Schema(name = "description",
            description = "Description of the error",
            type = "string",
            example = "Internal error occurred",
            nullable = true,
            required = true)
    @JsonProperty("description")
    private String description;
    @Schema(name = "timestamp", description = "Date of the error on ISO8601 format " + GlobalConstants.GATEWAY_DATE_FORMAT,
            type = "string",
            example = "2020-04-28T00:00:00.000Z",
            nullable = true,
            required = true)
    @JsonProperty("timestamp")
    private String timestamp;

    @Schema(name = "callId",
            description = "UUID of the external original call made to the gateway",
            type = "uuid",
            example = "ee507da7-fc0b-464a-a2ee-25847fa6a073",
            nullable = true,
            required = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("callId")
    private UUID callId;


    public GenericError() {
        this.timestamp = simpleDateFormat.format(new Date());
    }

    public GenericError(HttpStatus statusCode, Integer errorCode, String label, String description) {
        this.timestamp = simpleDateFormat.format(new Date());
        if (statusCode != null) {
            this.status = statusCode.value();
        } else {
            this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        this.errorCode = errorCode;
        this.label = label;
        this.description = description;
    }

    public GenericError(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        GenericError errorFromMessage;
        try {
            // load the GenericError from stringyfied error in the exception message
            errorFromMessage = objectMapper.readValue(message, GenericError.class);
            this.status = errorFromMessage.getStatus();
            this.label = errorFromMessage.getLabel();
            this.description = errorFromMessage.getDescription();
            this.errorCode = errorFromMessage.getErrorCode();
            this.timestamp = errorFromMessage.getTimestamp();
            this.callId = errorFromMessage.getCallId();
        } catch (JsonProcessingException jsonException) {
            // in case this is not a json in the message but un simple string, we set message with it with new Generic error object
            this.setDescription(message);
            this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
}
