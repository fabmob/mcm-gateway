package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.GatewayParams;
import com.gateway.database.repository.GatewayParamsRepository;
import com.gateway.database.service.GatewayParamsDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
public class GatewayParamsDatabaseServiceImplTest {
    @TestConfiguration
    public static class GatewayParamsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public GatewayParamsDatabaseService gatewayParamsDatabaseService(GatewayParamsRepository gatewayParamsRepository) {
            return new GatewayParamsDatabaseServiceImpl();
        }
    }

    @MockBean
    private GatewayParamsRepository gatewayParamsRepository;

    @Autowired
    private GatewayParamsDatabaseService gatewayParamsDatabaseService;

    @MockBean
    private ErrorMessages errorMessages;

    @Test
    public void testAddGatewayParams() {
        GatewayParams gatewayParams1 = new GatewayParams(CACHE_ACTIVATION,"true");
        GatewayParams gatewayParams2 = new GatewayParams(null,"x");
        GatewayParams gatewayParams3 = new GatewayParams("existing_key","x");
        GatewayParams gatewayParams4 = new GatewayParams("x",null);
        GatewayParams gatewayParams5 = new GatewayParams("error","error");

        Mockito.when(gatewayParamsRepository.findById(CACHE_ACTIVATION)).thenReturn(Optional.empty());
        Mockito.when(gatewayParamsRepository.save(gatewayParams1)).thenReturn(gatewayParams1);
        Mockito.when(gatewayParamsRepository.findById("existing_key")).thenReturn(Optional.of(gatewayParams3));
        Mockito.when(gatewayParamsRepository.findById("x")).thenReturn(Optional.empty());
        Mockito.when(gatewayParamsRepository.save(gatewayParams4)).thenReturn(gatewayParams4);
        Mockito.when(gatewayParamsRepository.findById("error")).thenReturn(Optional.empty());
        Mockito.when(gatewayParamsRepository.save(gatewayParams5)).thenThrow(new InternalException("error from db"));

        GatewayParams paramCreated1 = gatewayParamsDatabaseService.addGatewayParams(gatewayParams1);
        assertEquals(gatewayParams1, paramCreated1);
        assertThrows(ConflictException.class, () -> gatewayParamsDatabaseService.addGatewayParams(gatewayParams3));
        assertThrows(ConflictException.class, () -> gatewayParamsDatabaseService.addGatewayParams(gatewayParams2));
        assertEquals(gatewayParams4, gatewayParamsDatabaseService.addGatewayParams(gatewayParams4));
        assertThrows(InternalException.class, () -> gatewayParamsDatabaseService.addGatewayParams(gatewayParams5));
    }

    @Test
    public void testGetAllGatewayParams(){
        GatewayParams gatewayParams1 = new GatewayParams(CACHE_ACTIVATION,"true");
        GatewayParams gatewayParams2 = new GatewayParams("existing_key","x");
        List<GatewayParams> gatewayParams = new ArrayList<>();
        gatewayParams.add(gatewayParams1);
        gatewayParams.add(gatewayParams2);

        Mockito.when(gatewayParamsRepository.findAll()).thenReturn(gatewayParams);
        assertEquals(gatewayParams, gatewayParamsDatabaseService.getAllGatewayParams());

    }

    @Test
    public void testFindGatewayParamsByParamKey(){
        GatewayParams gatewayParams1 = new GatewayParams(CACHE_ACTIVATION,"true");

        Mockito.when(gatewayParamsRepository.findById(CACHE_ACTIVATION)).thenReturn(Optional.of(gatewayParams1));
        Mockito.when(gatewayParamsRepository.findById("key")).thenReturn(Optional.empty());
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("Request {0} not found");

        assertEquals(gatewayParams1, gatewayParamsDatabaseService.findGatewayParamsByParamKey(CACHE_ACTIVATION));
        assertThrows(NotFoundException.class, () -> gatewayParamsDatabaseService.findGatewayParamsByParamKey("key"));

    }

    @Test
    public void testUpdateGatewayParams(){
        GatewayParams gatewayParams1 = new GatewayParams(CACHE_ACTIVATION,"true");
        GatewayParams gatewayParams2 = new GatewayParams();
        gatewayParams2.setParamValue("false");
        GatewayParams gatewayParams3 = new GatewayParams(CACHE_ACTIVATION,"false");

        Mockito.when(gatewayParamsRepository.findById(CACHE_ACTIVATION)).thenReturn(Optional.of(gatewayParams1));
        Mockito.when(gatewayParamsRepository.save(ArgumentMatchers.any())).thenReturn(gatewayParams3);
        Mockito.when(gatewayParamsRepository.findById("key")).thenReturn(null);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("Request {0} not found");

        assertEquals(gatewayParams3, gatewayParamsDatabaseService.updateGatewayParams(CACHE_ACTIVATION,gatewayParams2));
        assertThrows(NotFoundException.class, () -> gatewayParamsDatabaseService.updateGatewayParams("key",gatewayParams2));

    }

    @Test
    public void testDeleteGatewayParams(){
        doNothing().when(gatewayParamsRepository).deleteById("key");
        NotFoundException exception = new NotFoundException("error");
        doThrow(exception).when(gatewayParamsRepository).deleteById("existing_key");
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("Request {0} not found");

        assertDoesNotThrow(() -> gatewayParamsDatabaseService.deleteGatewayParams("key"));
        assertThrows(NotFoundException.class, () -> gatewayParamsDatabaseService.deleteGatewayParams("existing_key"));

    }
}
