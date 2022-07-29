package com.gateway.requestrelay.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.requestrelay.*;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.requestrelay.service.RequestRelayService;
import com.gateway.requestrelay.utils.CustomParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
public class RequestRelayServiceImpl implements RequestRelayService {


    @Autowired
    private ErrorMessages errorMessages;


    /**
     * Process a call.
     *
     * @param callInfos Information about the call.
     * @return Response body.
     *
     */
    @Override
    public String processCalls(MspCallsFinalDTO callInfos)  {
        HttpHeaders httpHeaders = assignHeaders(callInfos);
        String body = callInfos.getBody();
        // Filling in the HTTP entity
        log.info("Processing body...");
        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);

        // Process http call
        log.info("Processing call : {} {}", callInfos.getMethod(), callInfos.getUrl());
        ResponseEntity<String> response = makeCall(entity, callInfos);

        return this.convertToUtf8(response);
    }


    /**
     * Assign parameterized call headers with right values
     *
     * @param callInfos : A {@link MspCallsFinalDTO} object containing all information about the call
     * @return Object {@link HttpHeaders} contains assigned headers
     */
    @Override
    public HttpHeaders assignHeaders(MspCallsFinalDTO callInfos)  {
        log.info("Processing Headers...");
        Set<HeadersValuesTemplateFinalDTO> headers = callInfos.getHeaders();
        final HttpHeaders httpHeaders = new HttpHeaders();

        // Process headers
        if(headers != null && !headers.isEmpty()){
            for(HeadersValuesTemplateFinalDTO header : headers){
                httpHeaders.add(header.getKey(), header.getValue());
            }
        }

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
    public ResponseEntity<String> makeCall(@SuppressWarnings("rawtypes") HttpEntity requestEntity, MspCallsFinalDTO callInfos) throws  BadGatewayException, UnavailableException {

        // get the correlationId of the current thread
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;

        try {

            response = restTemplate.exchange(callInfos.getUrl(), Objects.requireNonNull(HttpMethod.resolve(callInfos.getMethod())),
                    requestEntity, String.class);


        } catch (HttpClientErrorException e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),callInfos.getUrl()));
        } catch (RestClientException e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),callInfos.getUrl()));
        } catch (Exception e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),callInfos.getUrl()));
        }
        return response;
    }


    /**
     * Convert UTF-16 responses' bodies into UTF-8
     * @param response
     * @return responseBody in UTF-8
     */
    private String convertToUtf8(ResponseEntity<String> response){
        String responseBody = null;
        if (!response.getStatusCode().isError()) {
            responseBody = response.getBody();
            boolean isUtf16 = CustomParamUtils.isUtf16ContentType(response.getHeaders());
            if (isUtf16) {
                responseBody = CustomParamUtils.convertUtf16ToUtf8(response);
            }
        }

        return responseBody;
    }


}
