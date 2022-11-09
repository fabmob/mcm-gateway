package com.gateway.commonapi.properties;


import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.ServiceUnavailable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Config file to retrieve list of error code and error labels
 */
@Data
@Configuration
public class ErrorMessages {

    @Value("${error.technical.generic.code}")
    private String technicalGenericErrorCode;

    @Value("${error.technical.generic.label}")
    private String technicalGenericLabel;

    @Value("${error.technical.generic.description}")
    private String technicalGenericDescription;

    @Value("${error.technical.methodNotAllowed.code}")
    private String technicalMethodNotAllowedCode;

    @Value("${error.technical.methodNotAllowed.label}")
    private String technicalMethodNotAllowedLabel;

    @Value("${error.technical.methodNotAllowed.description}")
    private String technicalMethodNotAllowedDescription;

    @Value("${error.technical.mediaTypeNotAcceptable.code}")
    private String technicalMediaTypeNotAcceptableCode;
    @Value("${error.technical.mediaTypeNotAcceptable.label}")
    private String technicalMediaTypeNotAcceptableLabel;
    @Value("${error.technical.mediaTypeNotAcceptable.description}")
    private String technicalMediaTypeNotAcceptableDescription;

    @Value("${error.technical.missingServletRequestParam.code}")
    private String technicalMissingServletRequestParamCode;
    @Value("${error.technical.missingServletRequestParam.label}")
    private String technicalMissingServletRequestParamLabel;
    @Value("${error.technical.missingServletRequestParam.description}")
    private String technicalMissingServletRequestParamDescription;

    @Value("${error.technical.missingPathVariable.code}")
    private String technicalMissingPathVariableCode;
    @Value("${error.technical.missingPathVariable.label}")
    private String technicalMissingPathVariableLabel;
    @Value("${error.technical.missingPathVariable.description}")
    private String technicalMissingPathVariableDescription;

    @Value("${error.technical.requestBindingException.code}")
    private String technicalRequestBindingExceptionCode;

    @Value("${error.technical.conversionNotSupported.code}")
    private String technicalConversionNotSupportedCode;
    @Value("${error.technical.requestBindingException.label}")
    private String technicalRequestBindingExceptionLabel;
    @Value("${error.technical.requestBindingException.description}")
    private String technicalRequestBindingExceptionDescription;

    @Value("${error.technical.typeMismatchException.code}")
    private String technicalTypeMismatchExceptionCode;
    @Value("${error.technical.conversionNotSupported.label}")
    private String technicalConversionNotSupportedLabel;
    @Value("${error.technical.conversionNotSupported.description}")
    private String technicalConversionNotSupportedDescription;

    @Value("${error.technical.messageNotReadable.code}")
    private String technicalMessageNotReadableCode;
    @Value("${error.technical.typeMismatchException.label}")
    private String technicalTypeMismatchExceptionLabel;
    @Value("${error.technical.typeMismatchException.description}")
    private String technicalTypeMismatchExceptionDescription;

    @Value("${error.technical.messageNotWritable.code}")
    private String technicalMessageNotWritableCode;
    @Value("${error.technical.messageNotReadable.label}")
    private String technicalMessageNotReadableLabel;
    @Value("${error.technical.messageNotReadable.description}")
    private String technicalMessageNotReadableDescription;

    @Value("${error.technical.methodArgumentNotValid.code}")
    private String technicalMethodArgumentNotValidCode;
    @Value("${error.technical.messageNotWritable.label}")
    private String technicalMessageNotWritableLabel;
    @Value("${error.technical.messageNotWritable.description}")
    private String technicalMessageNotWritableDescription;

    @Value("${error.technical.missingServletRequestPart.code}")
    private String technicalMissingServletRequestPartCode;
    @Value("${error.technical.methodArgumentNotValid.label}")
    private String technicalMethodArgumentNotValidLabel;
    @Value("${error.technical.methodArgumentNotValid.description}")
    private String technicalMethodArgumentNotValidDescription;

    @Value("${error.technical.bindException.code}")
    private String technicalBindExceptionCode;
    @Value("${error.technical.missingServletRequestPart.label}")
    private String technicalMissingServletRequestPartLabel;
    @Value("${error.technical.missingServletRequestPart.description}")
    private String technicalMissingServletRequestPartDescription;

    @Value("${error.technical.noHandlerFound.code}")
    private String technicalNoHandlerFoundCode;
    @Value("${error.technical.bindException.label}")
    private String technicalBindExceptionLabel;
    @Value("${error.technical.bindException.description}")
    private String technicalBindExceptionDescription;

    @Value("${error.technical.httpRequestMethodNotSupported.code}")
    private String technicalHttpRequestMethodNotSupportedCode;
    @Value("${error.technical.noHandlerFound.label}")
    private String technicalNoHandlerFoundLabel;
    @Value("${error.technical.noHandlerFound.description}")
    private String technicalNoHandlerFoundDescription;

    @Value("${error.technical.resthttpclient}")
    private String technicalRestHttpClientError;
    @Value("${error.technical.httpRequestMethodNotSupported.label}")
    private String technicalHttpRequestMethodNotSupportedLabel;
    @Value("${error.technical.httpRequestMethodNotSupported.description}")
    private String technicalHttpRequestMethodNotSupportedDescription;

    @Value("${error.technical.badRequest.code}")
    private String technicalBadRequestCode;
    @Value("${error.technical.badRequest.label}")
    private String technicalBadRequestLabel;
    @Value("${error.technical.badRequest.description}")
    private String technicalBadRequestDescription;

    @Value("${error.technical.badGateway.code}")
    private String technicalBadGatewayCode;
    @Value("${error.technical.badGateway.label}")
    private String technicalBadGatewayLabel;
    @Value("${error.technical.badGateway.description}")
    private String technicalBadGatewayDescription;

    @Value("${error.technical.notFound.code}")
    private String technicalNotFoundCode;
    @Value("${error.technical.notFound.label}")
    private String technicalNotFoundLabel;
    @Value("${error.technical.notFound.description}")
    private String technicalNotFoundDescription;

    @Value("${error.technical.notProvided.description}")
    private String technicalNotProvidedDescription;



    /**
     * Prepare a ServiceUnavailable object
     *
     * @param label
     * @param description
     * @return
     */
    private GenericError getMethodNotAllowedError(String label, String description) {
        GenericError error = new GenericError();
        error.setErrorCode(Integer.parseInt(this.technicalMethodNotAllowedCode));
        error.setLabel(StringUtils.isEmpty(label) ? this.technicalMethodNotAllowedLabel : label);
        error.setDescription(StringUtils.isEmpty(description) ? this.technicalMethodNotAllowedDescription : description);
        return error;
    }

    private ServiceUnavailable getGenericTechnicalError() {
        ServiceUnavailable serviceUnavailable = new ServiceUnavailable(500, this.technicalGenericLabel,  this.technicalGenericDescription);
        return serviceUnavailable;
    }
}
