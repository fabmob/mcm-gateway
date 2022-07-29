package com.gateway.adapter.service;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.data.TokenDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    boolean needAuthentication(MspMetaDTO mspMeta);

    TokenDTO getOrUpdateToken(MspActionDTO authenticationAction, MspMetaDTO mspMeta);
}
