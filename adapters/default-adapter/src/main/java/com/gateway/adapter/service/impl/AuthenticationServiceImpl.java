package com.gateway.adapter.service.impl;

import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.commonapi.dto.data.PartnerActionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public PartnerActionDTO needAuthenticationAction (List<PartnerActionDTO> actions) {
        int counter = 0;
        PartnerActionDTO authenticationAction = new PartnerActionDTO();
        for (PartnerActionDTO partnerActionDTO : actions) {
            if (partnerActionDTO.isAuthentication()) {
                authenticationAction = partnerActionDTO;
                counter++;
            }
        }
        if (counter > 1 ){
            log.warn(AdapterMessageDict.THERE_IS_MORE_THAN_ONE_AUTHENTICATION_ACTION);
        }
        return authenticationAction;
    }


    @Override
    public boolean needAuthentication(List<PartnerActionDTO> actions) {
        boolean isAuthentication = false;
        for (PartnerActionDTO partnerActionDTO : actions) {
            if (partnerActionDTO.isAuthentication()) {
                isAuthentication = true;
                break;
            }
        }
        return isAuthentication;
    }


}
