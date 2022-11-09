package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.PartnerActions;
import com.gateway.database.model.Selector;
import com.gateway.database.repository.PartnerActionsRepository;
import com.gateway.database.repository.SelectorRepository;
import com.gateway.database.service.PartnerActionsDatabaseService;
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
public class PartnerActionsDatabaseServiceImplTest {
    @TestConfiguration
    public static class PartnerActionsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public PartnerActionsDatabaseService partnerMetaDatabaseService() {
            return new PartnerActionsDatabaseServiceImpl();
        }
    }

    @MockBean
    private PartnerActionsRepository actionRepository;

    @MockBean
    private SelectorRepository selectorRepository;

    @MockBean
    private ErrorMessages errorMessages;

    @Autowired
    private PartnerActionsDatabaseService partnerActionsDatabaseService;

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void TestAddPartnerAction() {
        PartnerActions action = new PartnerActions();
        Selector selector = new Selector();
        Mockito.when(selectorRepository.findById(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(Optional.of(selector));
        Mockito.when(actionRepository.save(action)).thenReturn(action);

        PartnerActions partnerAction = partnerActionsDatabaseService.addPartnerAction(action);
        assertEquals(partnerAction, action);
    }

    @Test
    public void testGetAllPartnerActions() {
        List<PartnerActions> actionsList = new ArrayList<>();
        Mockito.when(actionRepository.findAll()).thenReturn(actionsList);
        List<PartnerActions> actionsListToGet = partnerActionsDatabaseService.getAllPartnerActions();
        assertEquals(actionsListToGet, actionsList);
    }

    @Test
    public void testUpdatePartnerAction() {
        PartnerActions action = new PartnerActions();
        UUID idPartnerAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        action.setPartnerActionId(idPartnerAction);
        Mockito.when(actionRepository.save(action)).thenReturn(action);
        Mockito.when(actionRepository.findById(idPartnerAction)).thenReturn(Optional.of(action));
        PartnerActions actionToUpdate = partnerActionsDatabaseService.updatePartnerAction(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), action);
        assertEquals(actionToUpdate, action);
    }

    @Test
    public void testUpdatePartnerActionExceptions() {
        PartnerActions action = new PartnerActions();
        UUID idPartnerAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        action.setPartnerActionId(idPartnerAction);
        action.setAction("PING");

        PartnerActions updatedAction = new PartnerActions();
        updatedAction.setPartnerActionId(idPartnerAction);
        updatedAction.setAction("otherAction");

        Mockito.when(actionRepository.save(action)).thenReturn(action);

        Mockito.when(actionRepository.findById(updatedAction.getPartnerActionId())).thenReturn(Optional.of(updatedAction));
        partnerActionsDatabaseService.updatePartnerAction(idPartnerAction, updatedAction);
        assertThrows(ConflictException.class, () -> partnerActionsDatabaseService.updatePartnerAction(idPartnerAction, action));
    }

    @Test
    public void testDeletePartnerAction() {
        PartnerActions action = new PartnerActions();
        UUID idPartnerAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        action.setPartnerActionId(idPartnerAction);
        partnerActionsDatabaseService.deletePartnerAction(idPartnerAction);
        verify(actionRepository, times(1)).deleteById(idPartnerAction);
    }

    @Test
    public void testFindPartnerActionById() {
        UUID idPartnerAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerActions action = new PartnerActions();
        action.setPartnerActionId(idPartnerAction);
        Mockito.when(actionRepository.findById(idPartnerAction)).thenReturn(Optional.of(action));
        PartnerActions actionToFind = partnerActionsDatabaseService.findPartnerActionById(idPartnerAction);
        assertEquals(actionToFind, action);
    }

    @Test
    public void testFindByPartnerMetaId() {
        UUID idPartnerAction = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerActions action = new PartnerActions();
        action.setPartnerActionId(idPartnerAction);
        List<PartnerActions> actionsList = new ArrayList<>();
        actionsList.add(action);
        Mockito.when(actionRepository.fetchByIdPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(actionsList);
        List<PartnerActions> actionsListToGet = partnerActionsDatabaseService.findByPartnerMetaId(idPartnerAction);
        assertEquals(actionsListToGet, actionsList);
    }

    @Test
    public void testFindByPartnerMetaIdNotFound() {
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        UUID idMeta = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        assertThrows(NotFoundException.class, () -> partnerActionsDatabaseService.findByPartnerMetaId(idMeta));
    }

    @Test
    public void testConstructorPartnerActionsDatabaseServiceImpl() {
        PartnerActionsDatabaseServiceImpl partnerActionsDatabaseServiceImpl =
                new PartnerActionsDatabaseServiceImpl(actionRepository, selectorRepository);
        assertEquals(actionRepository, partnerActionsDatabaseServiceImpl.getActionRepository());

    }
}