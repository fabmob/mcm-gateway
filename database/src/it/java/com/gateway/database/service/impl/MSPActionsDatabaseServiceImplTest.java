package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.MSPActions;
import com.gateway.database.model.Selector;
import com.gateway.database.repository.MSPActionsRepository;
import com.gateway.database.repository.SelectorRepository;
import com.gateway.database.service.MSPActionsDatabaseService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class MSPActionsDatabaseServiceImplTest {
    @TestConfiguration
    public static class MSPActionsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public MSPActionsDatabaseService mspMetaDatabaseService() {
            return new MSPActionsDatabaseServiceImpl();
        }
    }

    @MockBean
    private MSPActionsRepository actionRepository;

    @MockBean
    private SelectorRepository selctorRepository;

    @MockBean
    private ErrorMessages errorMessages;

    @Autowired
    private MSPActionsDatabaseService mspActionsDatabaseService;

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void TestAddMspAction() {
        MSPActions action = new MSPActions();
        Selector selector = new Selector();
        Mockito.when(selctorRepository.findById(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(Optional.of(selector));
        Mockito.when(actionRepository.save(action)).thenReturn(action);

        MSPActions mspAction = mspActionsDatabaseService.addMspAction(action);
        assertEquals(mspAction, action);
    }

    @Test
    public void testGetAllMspActions() {
        List<MSPActions> actionslist = new ArrayList<>();
        Mockito.when(actionRepository.findAll()).thenReturn(actionslist);
        List<MSPActions> actionslistToGet = mspActionsDatabaseService.getAllMspActions();
        assertEquals(actionslistToGet, actionslist);
    }

    @Test
    public void testUpdateMspAction() {
        MSPActions action = new MSPActions();
        UUID idMspAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        action.setMspActionId(idMspAction);
        Mockito.when(actionRepository.save(action)).thenReturn(action);
        MSPActions actionToUpdate = mspActionsDatabaseService.updateMspAction(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), action);
        assertEquals(actionToUpdate, action);
    }

    @Test
    public void testDeleteMspAction() {
        MSPActions action = new MSPActions();
        UUID idMspAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        action.setMspActionId(idMspAction);
        mspActionsDatabaseService.deleteMspAction(idMspAction);
        verify(actionRepository, times(1)).deleteById(idMspAction);
    }

    @Test
    public void testFindMspActionById() {
        UUID idMspAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPActions action = new MSPActions();
        action.setMspActionId(idMspAction);
        Mockito.when(actionRepository.findById(idMspAction)).thenReturn(Optional.of(action));
        MSPActions actionToFind = mspActionsDatabaseService.findMspActionById(idMspAction);
        assertEquals(actionToFind, action);
    }

    @Test
    public void testFindByMspMetaId() {
        UUID idMspAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MSPActions action = new MSPActions();
        action.setMspActionId(idMspAction);
        List<MSPActions> actionsList = new ArrayList<>();
        actionsList.add(action);
        Mockito.when(actionRepository.fetchByIdMspId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(actionsList);
        List<MSPActions> actionsListToGet = mspActionsDatabaseService.findByMspMetaId(idMspAction);
        assertEquals(actionsListToGet, actionsList);
    }

    @Test
    public void testFindByMspMetaIdNotFound() {
        MSPActions action = new MSPActions();
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            mspActionsDatabaseService.findByMspMetaId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        });
    }

    @Test
    public void testConstructorMSPActionsDatabaseServiceImpl() {
        MSPActionsDatabaseServiceImpl mspActionsDatabaseServiceImpl =
                new MSPActionsDatabaseServiceImpl(actionRepository, selctorRepository);
        assertEquals(actionRepository, mspActionsDatabaseServiceImpl.getActionRepository());

    }
}