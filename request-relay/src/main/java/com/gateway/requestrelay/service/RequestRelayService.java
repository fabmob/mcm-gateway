package com.gateway.requestrelay.service;

import com.gateway.commonapi.dto.requestrelay.PartnerCallsFinalDTO;
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
    ResponseEntity<String> processCalls(PartnerCallsFinalDTO callInfos, boolean preserveOriginalErrors) ;


    /**
     * Assign parameterized call headers with right values
     *
     * @param callInfos : A {@link PartnerCallsFinalDTO} object containing all information about the call
     * @return Object {@link HttpHeaders} contains assigned headers
     */
    HttpHeaders assignHeaders(PartnerCallsFinalDTO callInfos) ;


    /**
     *
     *
     * @return {@link ResponseEntity} contains HTTP response
     * @throws BadGatewayException
     * @throws UnavailableException
     *
     */

    /**
     * Make the HTTP call.
     *
     * @param requestEntity          the request to send
     * @param callInfos              data to build the call
     * @param preserveOriginalErrors true to preserve the called partner response, otherwise translate in gateway format
     * @return
     * @throws BadGatewayException
     * @throws UnavailableException
     */
    ResponseEntity<String> makeCall(@SuppressWarnings("rawtypes") HttpEntity requestEntity, PartnerCallsFinalDTO callInfos, boolean preserveOriginalErrors);
}
