package com.gateway.adapter.service.impl;

import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.commonapi.dto.data.MspActionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public MspActionDTO needAuthenticationAction (List<MspActionDTO> actions) {
        int counter = 0;
        MspActionDTO authenticationAction = new MspActionDTO();
        for (MspActionDTO mspActionDTO : actions) {
            if (mspActionDTO.isAuthentication()) {
                authenticationAction = mspActionDTO;
                counter++;
            }
        }
        if (counter > 1 ){
            log.warn(AdapterMessageDict.THERE_IS_MORE_THAN_ONE_AUTHENTICATION_ACTION);
        }
        return authenticationAction;
    }


    @Override
    public boolean needAuthentication(List<MspActionDTO> actions) {
        boolean isAuthentication = false;
        for (MspActionDTO mspActionDTO : actions) {
            if (mspActionDTO.isAuthentication()) {
                isAuthentication = true;
                break;
            }
        }
        return isAuthentication;
    }


}
