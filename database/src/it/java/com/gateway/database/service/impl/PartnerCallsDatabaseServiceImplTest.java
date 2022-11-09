package com.gateway.database.service.impl;


import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.*;
import com.gateway.database.repository.BodyParamsRepository;
import com.gateway.database.repository.BodyRepository;
import com.gateway.database.repository.PartnerCallsRepository;
import com.gateway.database.service.PartnerCallsDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class PartnerCallsDatabaseServiceImplTest {

    @TestConfiguration
    public static class PartnerCallsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public PartnerCallsDatabaseService partnerCallsDatabaseService() {
            return new PartnerCallsDatabaseServiceImpl();
        }
    }

    @Autowired
    private PartnerCallsDatabaseService partnerCallsDatabaseService;

    @MockBean
    private PartnerCallsRepository partnerCallsRepository;

    @MockBean
    private BodyRepository bodyRepository;

    @MockBean
    private ErrorMessages errorMessages;


    @MockBean
    private BodyParamsRepository bodyParamRepository;

    @Test
    public void testAddPartnerCall() {
        PartnerCalls partnercall = new PartnerCalls();
        partnercall.setParams(new HashSet<>());
        partnercall.setParamsMultiCalls(new HashSet<>());
        Mockito.when(partnerCallsRepository.save(partnercall)).thenReturn(partnercall);
        partnercall.setHeaders(new HashSet<>());
        PartnerCalls partnercallToAdd = partnerCallsDatabaseService.addPartnerCall(partnercall);
        assertEquals(partnercallToAdd, partnercall);
    }

    @Test
    public void testGetAllCalls() {
        List<PartnerCalls> calls = new ArrayList<>();
        Mockito.when(partnerCallsRepository.findAll()).thenReturn(calls);
        List<PartnerCalls> allcalls = partnerCallsDatabaseService.getAllCalls();
        assertEquals(allcalls, calls);
    }

    @Test
    public void testUpdatePartnerCall() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerCalls partnercall = new PartnerCalls();
        PartnerActions partnerAction = new PartnerActions();
        partnerAction.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        partnercall.setAction(partnerAction);
        partnercall.setPartnerCallId(id);
        Body body = new Body();
        Set<BodyParams> bodyParamsSet = new HashSet<>();
        Mockito.when(partnerCallsRepository.findById(id)).thenReturn(Optional.of(partnercall));
        BodyParams bodyParams = new BodyParams();
        bodyParams.setPrecision("precision");
        bodyParamsSet.add(bodyParams);
        body.setBodyParams(bodyParamsSet);
        partnercall.setBody(body);

        Mockito.when(bodyRepository.save(body)).thenReturn(body);
        Mockito.when(bodyParamRepository.save(bodyParams)).thenReturn(bodyParams);
        PartnerCalls partnerCallToUpdate = partnerCallsDatabaseService.updatePartnerCall(id, partnercall);
        assertEquals(partnerCallToUpdate, partnercall);
    }

    @Test
    public void testUpdatePartnerCalls() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerCalls partnercall = new PartnerCalls();
        PartnerActions PartnerAction = new PartnerActions();
        PartnerAction.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        partnercall.setAction(PartnerAction);
        partnercall.setPartnerCallId(id);
        Body body = new Body();
        body.setBodyId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f111"));
        Set<BodyParams> bodyParamsSet = new HashSet<>();
        BodyParams bodyParams = new BodyParams();
        bodyParams.setPrecision("precision");
        bodyParamsSet.add(bodyParams);
        body.setBodyParams(bodyParamsSet);
        partnercall.setBody(body);

        Mockito.when(bodyRepository.save(body)).thenReturn(body);
        Mockito.when(bodyParamRepository.save(bodyParams)).thenReturn(bodyParams);
        PartnerCalls partnerCallToUpdate = partnerCallsDatabaseService.updatePartnerCall(id, partnercall);
        assertEquals(partnerCallToUpdate, partnercall);
    }

    @Test
    public void testDeletePartnerCall() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        partnerCallsDatabaseService.deletePartnerCall(id);
        verify(partnerCallsRepository, times(1)).deleteById(id);
    }

    @Test
    public void testAddPartnerCallBodyId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Body body = new Body();
        body.setBodyId(id);
        PartnerCalls partnercall = new PartnerCalls();
        partnercall.setBody(body);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> partnerCallsDatabaseService.addPartnerCall(partnercall));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddPartnerCallHeadersId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Headers headers = new Headers();
        headers.setHeadersId(id);
        PartnerCalls partnercall = new PartnerCalls();
        Set<Headers> setH = new HashSet<>();
        setH.add(headers);
        partnercall.setHeaders(setH);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> partnerCallsDatabaseService.addPartnerCall(partnercall));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddPartnerCallParamsId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Params params = new Params();
        params.setParamsId(id);
        PartnerCalls partnercall = new PartnerCalls();
        Set<Params> setP = new HashSet<>();
        setP.add(params);
        partnercall.setParams(setP);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> partnerCallsDatabaseService.addPartnerCall(partnercall));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddPartnerCallParamsMultiCallsId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        ParamsMultiCalls paramsMultiCalls = new ParamsMultiCalls();
        paramsMultiCalls.setParamsMultiCallsId(id);
        PartnerCalls partnercall = new PartnerCalls();
        Set<ParamsMultiCalls> setParamsMultiCalls = new HashSet<>();
        setParamsMultiCalls.add(paramsMultiCalls);
        partnercall.setParamsMultiCalls(setParamsMultiCalls);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> partnerCallsDatabaseService.addPartnerCall(partnercall));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testFindPartnerCallsById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerCalls partnercall = new PartnerCalls();
        partnercall.setPartnerCallId(id);

        Mockito.when(partnerCallsRepository.findById(id)).thenReturn(Optional.of(partnercall));
        PartnerCalls partnerCallToFind = partnerCallsDatabaseService.findPartnerCallsById(id);
        assertEquals(partnerCallToFind, partnercall);
    }

    @Test
    public void testFindByActionPartnerActionId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerCalls partnercall = new PartnerCalls();
        partnercall.setPartnerCallId(id);
        List<PartnerCalls> partnerCallsList = new ArrayList<>();
        partnerCallsList.add(partnercall);

        Mockito.when(partnerCallsRepository.findByActionPartnerActionId(id)).thenReturn(partnerCallsList);
        List<PartnerCalls> partnerCallsListToFind = partnerCallsDatabaseService.findByActionPartnerActionId(id);
        assertEquals(partnerCallsList, partnerCallsListToFind);
    }


    @Test
    public void testPartnerCallsDatabaseServiceImpl() {
        PartnerCallsDatabaseServiceImpl partnerCallsDatabaseServiceImpl =
                new PartnerCallsDatabaseServiceImpl(partnerCallsRepository, bodyRepository, bodyParamRepository);
        assertEquals(bodyRepository, partnerCallsDatabaseServiceImpl.getBodyRepository());

    }
}