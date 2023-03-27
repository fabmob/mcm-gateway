package com.gateway.requestrelay.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.requestrelay.HeadersValuesTemplateFinalDTO;
import com.gateway.commonapi.dto.requestrelay.PartnerCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.restConfig.RestConfig;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.SanitizorUtils;
import com.gateway.requestrelay.service.RequestRelayService;
import com.gateway.requestrelay.utils.CustomParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.gateway.requestrelay.utils.constant.RequestRelayMessageDict.CALL_ID;
import static com.gateway.requestrelay.utils.constant.RequestRelayMessageDict.UNSUPPORTED_SCHEME;

@Slf4j
@Service
public class RequestRelayServiceImpl implements RequestRelayService {


    @Autowired
    private ErrorMessages errorMessages;
    private static final String SEPARATOR = ": ";

    private static final List<String> ALLOWED_OUTBOUND_PROTOCOLS = List.of("http", "https");

    RestConfig restConfig = new RestConfig();
    RestTemplate restTemplate = restConfig.restTemplate();

    /**
     * Process a call.
     *
     * @param callInfos              Information about the call.
     * @param preserveOriginalErrors
     * @return Response body.
     */
    @Override
    public ResponseEntity<String> processCalls(PartnerCallsFinalDTO callInfos, boolean preserveOriginalErrors) {
        HttpHeaders httpHeaders = assignHeaders(callInfos);
        String body = callInfos.getBody();
        // Filling in the HTTP entity
        log.info("Processing body...");
        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);

        // Process http call
        log.info("Processing call : {} {}", SanitizorUtils.convertToSingleLine(callInfos.getMethod()), SanitizorUtils.convertToSingleLine(callInfos.getUrl()));
        ResponseEntity<String> response = makeCall(entity, callInfos, preserveOriginalErrors);

        return this.convertToUtf8(response, preserveOriginalErrors);
    }


    /**
     * Assign parameterized call headers with right values
     *
     * @param callInfos : A {@link PartnerCallsFinalDTO} object containing all information about the call
     * @return Object {@link HttpHeaders} contains assigned headers
     */
    @Override
    public HttpHeaders assignHeaders(PartnerCallsFinalDTO callInfos) {
        log.info("Processing Headers...");
        Set<HeadersValuesTemplateFinalDTO> headers = callInfos.getHeaders();
        final HttpHeaders httpHeaders = new HttpHeaders();

        // Process headers
        if (headers != null && !headers.isEmpty()) {
            for (HeadersValuesTemplateFinalDTO header : headers) {
                httpHeaders.add(SanitizorUtils.sanitizeHeader(header.getKey()), SanitizorUtils.sanitizeHeader(header.getValue()));
            }
        }

        // finally add the correlationId header
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));
        httpHeaders.add(GlobalConstants.CORRELATION_ID_HEADER, correlationId);

        return httpHeaders;
    }


    /**
     * Make the HTTP call.
     *
     * @return {@link ResponseEntity} contains HTTP response
     * @throws BadGatewayException
     * @throws UnavailableException
     */
    @Override
    public ResponseEntity<String> makeCall(@SuppressWarnings("rawtypes") HttpEntity requestEntity, PartnerCallsFinalDTO callInfos, boolean preserveOriginalErrors) {

        // get the correlationId of the current thread
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));


        ResponseEntity<String> response = null;

        try {
            if (!this.isSafeUrl(callInfos.getUrl())) {
                throw new BadRequestException(UNSUPPORTED_SCHEME);
            }

            response = restTemplate.exchange(callInfos.getUrl(), Objects.requireNonNull(HttpMethod.resolve(callInfos.getMethod())),
                    requestEntity, String.class);

        } catch (HttpClientErrorException e) {
            log.error(MessageFormat.format(CALL_ID, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), callInfos.getUrl()) + SEPARATOR + e.getResponseBodyAsString());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(CALL_ID, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), callInfos.getUrl()) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(CALL_ID, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), callInfos.getUrl()) + SEPARATOR + e.getMessage());
            }
        }
        return response;
    }


    /**
     * Convert UTF-16 responses' bodies into UTF-8
     *
     * @param response
     * @param preserveOriginalErrors
     * @return responseBody in UTF-8
     */
    private ResponseEntity<String> convertToUtf8(ResponseEntity<String> response, boolean preserveOriginalErrors) {
        String responseBody = null;
        if (preserveOriginalErrors && response.getStatusCode().isError() || !response.getStatusCode().isError()) {
            responseBody = response.getBody();
            boolean isUtf16 = CustomParamUtils.isUtf16ContentType(response.getHeaders());
            if (isUtf16) {
                responseBody = CustomParamUtils.convertUtf16ToUtf8(response);
            }
            response = new ResponseEntity<>(responseBody, response.getStatusCode().isError() ? response.getStatusCode() : HttpStatus.OK);
        } else {
            response = null;
        }
        return response;
    }

    /**
     * Return true if the url is safe, ie starts with http ot https
     * We nned to control the scheme as the URL comes from a database, so it can be altered to use different schemes
     * and perform internal network scans for example.
     *
     * @param url
     * @return true if the URL start with a supported scheme
     */
    private boolean isSafeUrl(String url) {
        boolean isSafe = false;
        if (StringUtils.isNotEmpty(url)) {
            isSafe = ALLOWED_OUTBOUND_PROTOCOLS.stream().anyMatch(url::startsWith);
        }

        if (!isSafe) {
            log.error(MessageFormat.format("Wrong scheme for partner URL {0}", SanitizorUtils.convertToSingleLine(url)));
        }
        return isSafe;
    }

}
