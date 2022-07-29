package com.gateway.requestrelay.service;

import com.gateway.commonapi.dto.requestrelay.MspCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.UnavailableException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


public interface RequestRelayService {

    /**
     * Process a call.
     *
     * @param callInfos Information about the call.
     * @return Response body.
     *
     */
    String processCalls(MspCallsFinalDTO callInfos) ;


    /**
     * Assign parameterized call headers with right values
     *
     * @param callInfos : A {@link MspCallsFinalDTO} object containing all information about the call
     * @return Object {@link HttpHeaders} contains assigned headers
     */
    HttpHeaders assignHeaders(MspCallsFinalDTO callInfos) ;


    /**
     * Make the HTTP call.
     *
     * @return {@link ResponseEntity} contains HTTP response
     * @throws BadGatewayException
     * @throws UnavailableException
     *
     */
    ResponseEntity<String> makeCall(@SuppressWarnings("rawtypes") HttpEntity requestEntity, MspCallsFinalDTO callInfos) throws BadGatewayException, UnavailableException;
}
