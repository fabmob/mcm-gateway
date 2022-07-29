package com.gateway.adapter.service.impl;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.properties.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private ErrorMessages errorMessage;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testNeedAuthenticationAction() {
        List<MspActionDTO> actions = new ArrayList<>();
        MspActionDTO actionDTO = new MspActionDTO();
        actionDTO.setAuthentication(true);
        actions.add(actionDTO);
        MspActionDTO authenticationAction = authenticationService.needAuthenticationAction( actions);
        assertEquals(authenticationAction.isAuthentication(), true);

    }

    @Test
    void testNeedAuthentication() {
        List<MspActionDTO> actions = new ArrayList<>();
        MspActionDTO actionDTO = new MspActionDTO();
        actionDTO.setAuthentication(true);
        actions.add(actionDTO);
        Boolean isAuthentication = authenticationService.needAuthentication( actions);
        assertEquals(isAuthentication, true);
    }
}