package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.*;
import com.gateway.database.repository.AdaptersRepository;
import com.gateway.database.repository.PartnerActionsRepository;
import com.gateway.database.repository.PartnerMetaRepository;
import com.gateway.database.repository.PartnerStandardRepository;
import com.gateway.database.service.PartnerStandardDatabaseService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class PartnerStandardDatabaseServiceImplTest {
    @TestConfiguration
    public static class PartnerCallsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public PartnerStandardDatabaseService PartnerStandardDatabaseServiceImp() {
            return new PartnerStandardDatabaseServiceImpl();
        }
    }

    @Autowired
    private PartnerStandardDatabaseService partnerStandardDatabaseService;

    @MockBean
    private PartnerActionsDatabaseServiceImpl partnerActionsDatabaseService;

    @MockBean
    private PartnerMetaDatabaseServiceImpl partnerMetaDatabaseService;

    @MockBean
    private AdaptersDatabaseServiceImpl adaptersDatabaseService;

    @MockBean
    PartnerStandardRepository partnerStandardRepository;

    @MockBean
    PartnerActionsRepository partnerActionsRepository;

    @MockBean
    PartnerMetaRepository partnerMetaRepository;

    @MockBean
    AdaptersRepository adaptersRepository;

    @MockBean
    private ErrorMessages errorMessages;

    public StandardPK initPK() {
        StandardPK standardPK = new StandardPK();
        PartnerActions partnerActions = new PartnerActions();
        PartnerMeta partner = new PartnerMeta();

        partnerActions.setPartnerActionId(UUID.randomUUID());
        partner.setPartnerId(UUID.randomUUID());

        standardPK.setPartner(partner);
        standardPK.setVersionDataMapping("2.X.X");
        standardPK.setVersionStandard("2.X.Y");
        standardPK.setAction(partnerActions);

        return standardPK;
    }

    @Test
    public void addPartnerStandard() {
        PartnerStandard standard = new PartnerStandard();
        standard.setId(initPK());
        PartnerMeta partnerMeta = new PartnerMeta();
        Adapters adapter = new Adapters();
        PartnerActions action = new PartnerActions();

        UUID partnerMetaId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f889");
        UUID idPartnerStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID adapterId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f887");
        UUID actionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f887");

        action.setPartnerActionId(actionId);
        action.setAction("PING");
        adapter.setAdapterId(adapterId);
        partnerMeta.setPartnerId(partnerMetaId);
        partnerMeta.setHasPing(true);
        standard.getId().setPartner(partnerMeta);
        standard.setPartnerStandardId(idPartnerStandard);
        standard.setAdapter(adapter);

        Mockito.when(partnerStandardRepository.findByPartnerStandardId(idPartnerStandard)).thenReturn(standard);
        Mockito.when(partnerActionsDatabaseService.findPartnerActionById(standard.getId().getAction().getPartnerActionId())).thenReturn(action);
        Mockito.when(partnerMetaDatabaseService.findPartnerMetaById(partnerMetaId)).thenReturn(partnerMeta);
        Mockito.when(adaptersRepository.findById(partnerMetaId)).thenReturn(Optional.of(adapter));
        partnerStandardDatabaseService.deletePartnerStandard(standard.getPartnerStandardId());
        Mockito.when(partnerStandardRepository.save(standard)).thenReturn(standard);

        PartnerStandard partnerStandardToAdd = partnerStandardDatabaseService.addPartnerStandard(standard);
        assertEquals(partnerStandardToAdd, standard);
    }

    @Test
    public void updatePartnerStandard() {
        PartnerStandard standard = new PartnerStandard();
        standard.setId(initPK());
        PartnerMeta partnerMeta = new PartnerMeta();
        Adapters adapter = new Adapters();
        PartnerActions action = new PartnerActions();

        UUID partnerMetaId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f889");
        UUID idPartnerStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID adapterId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f887");
        UUID actionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f887");

        action.setPartnerActionId(actionId);
        action.setAction("PING");
        adapter.setAdapterId(adapterId);
        partnerMeta.setPartnerId(partnerMetaId);
        partnerMeta.setHasPing(true);
        standard.getId().setPartner(partnerMeta);
        standard.setPartnerStandardId(idPartnerStandard);
        standard.setAdapter(adapter);

        Mockito.when(partnerStandardRepository.findByPartnerStandardId(idPartnerStandard)).thenReturn(standard);
        Mockito.when(partnerActionsDatabaseService.findPartnerActionById(standard.getId().getAction().getPartnerActionId())).thenReturn(action);
        Mockito.when(partnerMetaDatabaseService.findPartnerMetaById(partnerMetaId)).thenReturn(partnerMeta);
        Mockito.when(adaptersRepository.findById(partnerMetaId)).thenReturn(Optional.of(adapter));
        partnerStandardDatabaseService.deletePartnerStandard(standard.getPartnerStandardId());
        Mockito.when(partnerStandardRepository.save(standard)).thenReturn(standard);

        PartnerStandard standardToUpdate = partnerStandardDatabaseService.updatePartnerStandard(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), standard);
        assertEquals(standardToUpdate, standard);
    }

    @Test
    public void updatePartnerStandardException() {
        PartnerStandard partnerStandard = new PartnerStandard();
        partnerStandard.setId(initPK());
        partnerStandard.setAdapter(new Adapters(UUID.randomUUID(), "ADAPTER"));
        Mockito.when(adaptersDatabaseService.findAdapterById(any(UUID.class))).thenThrow(new NotFoundException(""));
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("");
        UUID idPartner = partnerStandard.getId().getPartner().getPartnerId();
        assertThrows(NotFoundException.class, () -> partnerStandardDatabaseService.updatePartnerStandard(idPartner, partnerStandard));
    }

    @Test
    public void deletePartnerStandard() {
        PartnerStandard standard = new PartnerStandard();
        UUID idPartnerStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        StandardPK pk = new StandardPK();
        standard.setId(pk);
        standard.setPartnerStandardId(idPartnerStandard);
        Mockito.when(partnerStandardRepository.findByPartnerStandardId(idPartnerStandard)).thenReturn(standard);
        partnerStandardDatabaseService.deletePartnerStandard(standard.getPartnerStandardId());
        verify(partnerStandardRepository, times(1)).deleteById(pk);
    }

    @Test
    public void findPartnerStandardById() {
        UUID idPartnerStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerStandard standard = new PartnerStandard();
        standard.setPartnerStandardId(idPartnerStandard);
        Mockito.when(partnerStandardRepository.findByPartnerStandardId(idPartnerStandard)).thenReturn(standard);
        PartnerStandard standardToFind = partnerStandardDatabaseService.findPartnerStandardById(idPartnerStandard);
        assertEquals(standardToFind, standard);
    }

    @Test
    public void getByCriteria() {
        PartnerStandard standard = new PartnerStandard();
        PartnerMeta partner = new PartnerMeta();
        PartnerActions action = new PartnerActions();
        UUID partnerMetaId = UUID.fromString("a457579d-02f8-4479-b97b-ffb678e3f888");
        UUID partnerActionsId = UUID.fromString("d457579d-02f8-4479-b97b-ffb678e3f888");
        String partnerActionsName = "";
        String versionStandard = "V1";
        String versionDatamapping = "V2";
        Boolean isActive = false;
        partner.setPartnerId(partnerMetaId);
        action.setPartnerActionId(partnerActionsId);
        StandardPK pk = new StandardPK();
        pk.setVersionStandard(versionStandard);
        pk.setVersionDataMapping(versionDatamapping);
        pk.setPartner(partner);
        pk.setAction(action);
        standard.setId(pk);
        standard.setIsActive(isActive);
        List<PartnerStandard> partnerStandards = new ArrayList<>();
        partnerStandards.add(standard);
        Mockito.when(partnerStandardRepository.findByKeyPrimary(partnerMetaId, partnerActionsId, partnerActionsName, versionStandard, versionDatamapping, isActive)).thenReturn(partnerStandards);
        List<PartnerStandard> allPartnerStandards = partnerStandardDatabaseService.getByCriteria(partnerMetaId, partnerActionsId, partnerActionsName, versionStandard, versionDatamapping, isActive);
        assertEquals(allPartnerStandards, partnerStandards);

    }

    @Test
    public void testFindByPartnerStandardIdNotFound() {
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        UUID standardUuidToFind = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> partnerStandardDatabaseService.findPartnerStandardById(standardUuidToFind));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testGetAllPartnerStandard() {
        List<PartnerStandard> partnerStandards = new ArrayList<>();
        Mockito.when(partnerStandardRepository.findAll()).thenReturn(partnerStandards);
        List<PartnerStandard> partnerStandardsListToGet = partnerStandardDatabaseService.getAllPartnerStandard();
        assertEquals(partnerStandardsListToGet, partnerStandards);
    }

    @Test
    public void deletePartnerStandardKo() {
        PartnerStandard standard = new PartnerStandard();
        UUID idPartnerStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        StandardPK pk = new StandardPK();
        standard.setId(pk);
        standard.setPartnerStandardId(idPartnerStandard);
        Mockito.when(partnerStandardRepository.findByPartnerStandardId(idPartnerStandard)).thenReturn(null);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        UUID standardUuidToFind = standard.getPartnerStandardId();
        NotFoundException exception = assertThrows(NotFoundException.class, () -> partnerStandardDatabaseService.deletePartnerStandard(standardUuidToFind));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testStandardsKo() {
        UUID idPartnerMeta = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> partnerStandardDatabaseService.getByCriteria(idPartnerMeta, null, null, null, null, null));
        String actualMessage = exception.getMessage();
        assertEquals("PartnerStandard with (PartnerMeta id f457579d-02f8-4479-b97b-ffb678e3f888 ,PartnerActions id null,PartnerActions name null, Version Standard null, Version Datamapping null,Active null)", actualMessage);
    }

    @Test
    public void testAddPartnerStandardConflictException() {
        UUID partnerStandardIDInDB = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerStandard partnerStandardInDB = new PartnerStandard();
        partnerStandardInDB.setPartnerStandardId(partnerStandardIDInDB);

        Optional<PartnerStandard> partnerStandardToAdd = Optional.of(partnerStandardInDB);
        PartnerMeta partnerMeta = new PartnerMeta();
        partnerMeta.setPartnerId(UUID.randomUUID());
        PartnerActions partnerActions = new PartnerActions();
        partnerActions.setPartnerActionId(UUID.randomUUID());

        StandardPK standardPK = new StandardPK();
        standardPK.setVersionStandard("2.x");
        standardPK.setVersionDataMapping("2.x");
        standardPK.setPartner(partnerMeta);
        standardPK.setAction(partnerActions);
        partnerStandardToAdd.get().setId(standardPK);
        partnerStandardInDB.setId(standardPK);

        Mockito.when(partnerStandardRepository.findById(partnerStandardToAdd.get().getId())).thenReturn(Optional.of(partnerStandardInDB));

        PartnerStandard partnerStandardToAddGet = partnerStandardToAdd.get();
        assertThrows(ConflictException.class, () -> partnerStandardDatabaseService.addPartnerStandard(partnerStandardToAddGet));
    }

    @Test
    public void testAddPartnerStandardNotFoundException() {
        PartnerStandard partnerStandard = new PartnerStandard();
        partnerStandard.setId(initPK());
        partnerStandard.setAdapter(new Adapters(UUID.randomUUID(), "ADAPTER"));
        Mockito.when(adaptersDatabaseService.findAdapterById(any(UUID.class))).thenThrow(new NotFoundException(""));
        assertThrows(NotFoundException.class, () -> partnerStandardDatabaseService.addPartnerStandard(partnerStandard));
    }
}