package com.gateway.adapter.service.impl;

import com.gateway.adapter.service.AuthenticationService;
import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.data.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Override
    public boolean needAuthentication(MspMetaDTO mspMeta) {
        // TODO manage in US https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/100
        return false;
    }

    @Override
    public TokenDTO getOrUpdateToken(MspActionDTO authenticationAction, MspMetaDTO mspMeta) {
        // TODO manage in US https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/100
        return new TokenDTO();
    }
}
