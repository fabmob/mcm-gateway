package com.gateway.adapter.service.impl;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private ErrorMessages errorMessage;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testNeedAuthenticationAction() {
        List<PartnerActionDTO> actions = new ArrayList<>();
        PartnerActionDTO actionDTO = new PartnerActionDTO();
        actionDTO.setAuthentication(true);
        actions.add(actionDTO);
        PartnerActionDTO authenticationAction = authenticationService.needAuthenticationAction(actions);
        assertTrue(authenticationAction.isAuthentication());

    }

    @Test
    void testNeedAuthentication() {
        List<PartnerActionDTO> actions = new ArrayList<>();
        PartnerActionDTO actionDTO = new PartnerActionDTO();
        actionDTO.setAuthentication(true);
        actions.add(actionDTO);
        Boolean isAuthentication = authenticationService.needAuthentication(actions);
        assertEquals(true, isAuthentication);
    }
}