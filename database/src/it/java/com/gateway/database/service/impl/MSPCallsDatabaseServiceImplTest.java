package com.gateway.database.service.impl;


import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.*;
import com.gateway.database.repository.BodyParamsRepository;
import com.gateway.database.repository.BodyRepository;
import com.gateway.database.repository.MSPCallsRepository;
import com.gateway.database.service.MSPCallsDatabaseService;
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
public class MSPCallsDatabaseServiceImplTest {

    @TestConfiguration
    public static class MSPCallsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public MSPCallsDatabaseService mspCallsDatabaseService() {
            return new MSPCallsDatabaseServiceImpl();
        }
    }

    @Autowired
    private MSPCallsDatabaseService mspCallsDatabaseService;

    @MockBean
    private MSPCallsRepository mspCallsRepository;

    @MockBean
    private BodyRepository bodyRepository;

    @MockBean
    private ErrorMessages errorMessages;


    @MockBean
    private BodyParamsRepository bodyParamRepository;

    @Test
    public void testAddMspCall() {
        MSPCalls mspcall = new MSPCalls();
        mspcall.setParams(new HashSet<>());
        mspcall.setParamsMultiCalls(new HashSet<>());
        Mockito.when(mspCallsRepository.save(mspcall)).thenReturn(mspcall);
        mspcall.setHeaders(new HashSet<>());
        MSPCalls mspcallToAdd = mspCallsDatabaseService.addMspCall(mspcall);
        assertEquals(mspcallToAdd, mspcall);
    }

    @Test
    public void testGetAllCalls() {
        List<MSPCalls> calls = new ArrayList<>();
        Mockito.when(mspCallsRepository.findAll()).thenReturn(calls);
        List<MSPCalls> allcalls = mspCallsDatabaseService.getAllCalls();
        assertEquals(allcalls, calls);
    }

    @Test
    public void testUpdateMspCall() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPCalls mspcall = new MSPCalls();
        MSPActions mspAction = new MSPActions();
        mspAction.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        mspcall.setAction(mspAction);
        mspcall.setMspCallId(id);
        Body body = new Body();
        Set<BodyParams> bodyParamsSet = new HashSet<>();
        Mockito.when(mspCallsRepository.findById(id)).thenReturn(Optional.of(mspcall));
        BodyParams bodyParams = new BodyParams();
        bodyParams.setPrecision("precision");
        bodyParamsSet.add(bodyParams);
        body.setBodyParams(bodyParamsSet);
        mspcall.setBody(body);

        Mockito.when(bodyRepository.save(body)).thenReturn(body);
        Mockito.when(bodyParamRepository.save(bodyParams)).thenReturn(bodyParams);
        MSPCalls MSPCallToUpdate = mspCallsDatabaseService.updateMspCall(id, mspcall);
        assertEquals(MSPCallToUpdate, mspcall);
    }

    @Test
    public void testUpdateMspCalls() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPCalls mspcall = new MSPCalls();
        MSPActions mspAction = new MSPActions();
        mspAction.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        mspcall.setAction(mspAction);
        mspcall.setMspCallId(id);
        Body body = new Body();
        body.setBodyId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f111"));
        Set<BodyParams> bodyParamsSet = new HashSet<>();
        BodyParams bodyParams = new BodyParams();
        bodyParams.setPrecision("precision");
        bodyParamsSet.add(bodyParams);
        body.setBodyParams(bodyParamsSet);
        mspcall.setBody(body);

        Mockito.when(bodyRepository.save(body)).thenReturn(body);
        Mockito.when(bodyParamRepository.save(bodyParams)).thenReturn(bodyParams);
        MSPCalls MSPCallToUpdate = mspCallsDatabaseService.updateMspCall(id, mspcall);
        assertEquals(MSPCallToUpdate, mspcall);
    }

    @Test
    public void testDeleteMspCall() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        mspCallsDatabaseService.deleteMspCall(id);
        verify(mspCallsRepository, times(1)).deleteById(id);
    }

    @Test
    public void testAddMspCallBodyId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Body body = new Body();
        body.setBodyId(id);
        MSPCalls mspcall = new MSPCalls();
        mspcall.setBody(body);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            mspCallsDatabaseService.addMspCall(mspcall);
        });
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddMspCallHeadersId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Headers headers = new Headers();
        headers.setHeadersId(id);
        MSPCalls mspcall = new MSPCalls();
        Set<Headers> setH = new HashSet<>();
        setH.add(headers);
        mspcall.setHeaders(setH);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            mspCallsDatabaseService.addMspCall(mspcall);
        });
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddMspCallParamsId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Params params = new Params();
        params.setParamsId(id);
        MSPCalls mspcall = new MSPCalls();
        Set<Params> setP = new HashSet<>();
        setP.add(params);
        mspcall.setParams(setP);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            mspCallsDatabaseService.addMspCall(mspcall);
        });
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testAddMspCallParamsMultiCallsId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        ParamsMultiCalls paramsMultiCalls = new ParamsMultiCalls();
        paramsMultiCalls.setParamsMultiCallsId(id);
        MSPCalls mspcall = new MSPCalls();
        Set<ParamsMultiCalls> setParamsMultiCalls = new HashSet<>();
        setParamsMultiCalls.add(paramsMultiCalls);
        mspcall.setParamsMultiCalls(setParamsMultiCalls);
        Mockito.when(errorMessages.getTechnicalNotProvidedDescription()).thenReturn("should throw exception");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            mspCallsDatabaseService.addMspCall(mspcall);
        });
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testFindMspCallsById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPCalls mspcall = new MSPCalls();
        mspcall.setMspCallId(id);

        Mockito.when(mspCallsRepository.findById(id)).thenReturn(Optional.of(mspcall));
        MSPCalls mspCallToFind = mspCallsDatabaseService.findMspCallsById(id);
        assertEquals(mspCallToFind, mspcall);
    }

    @Test
    public void testFindByActionMspActionId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPCalls mspcall = new MSPCalls();
        mspcall.setMspCallId(id);
        List<MSPCalls> mspCallsList = new ArrayList<>();
        mspCallsList.add(mspcall);

        Mockito.when(mspCallsRepository.findByActionMspActionId(id)).thenReturn(mspCallsList);
        List<MSPCalls> mspCallsListToFind = mspCallsDatabaseService.findByActionMspActionId(id);
        assertEquals(mspCallsList, mspCallsListToFind);
    }


    @Test
    public void testMSPCallsDatabaseServiceImpl() {
        MSPCallsDatabaseServiceImpl mspCallsDatabaseServiceImpl =
                new MSPCallsDatabaseServiceImpl(mspCallsRepository, bodyRepository, bodyParamRepository);
        assertEquals(bodyRepository, mspCallsDatabaseServiceImpl.getBodyRepository());

    }
}