package com.gateway.adapter.service;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * all authentication operations
 */
@Service
public interface AuthenticationService {

    /**
     * looping for all actions of an mspMeta and check if it exists at least one operation that needs authentication
     *
     * @return the authentication action
     */
    PartnerActionDTO needAuthenticationAction(List<PartnerActionDTO> actions);

    /**
     * if isAuthentication is true , the msp need authentication and the methode return true
     *
     * @param actions
     * @return true or false
     */
    boolean needAuthentication(List<PartnerActionDTO> actions);

}
