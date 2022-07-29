package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.*;
import com.gateway.database.repository.AdaptersRepository;
import com.gateway.database.repository.MSPActionsRepository;
import com.gateway.database.repository.MspMetaRepository;
import com.gateway.database.repository.MspStandardRepository;
import com.gateway.database.service.MspStandardDatabaseService;
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
public class MspStandardDatabaseServiceImplTest {
    @TestConfiguration
    public static class MSPCallsDatabaseServiceImplTestContextConfiguration {
        @Bean
        public MspStandardDatabaseService MspStandardDatabaseServiceImp() {
            return new MspStandardDatabaseServiceImpl();
        }
    }

    @Autowired
    private MspStandardDatabaseService mspStandardDatabaseService;

    @MockBean
    private MSPActionsDatabaseServiceImpl mspActionsDatabaseService;

    @MockBean
    private MspMetaDatabaseServiceImpl mspMetaDatabaseService;

    @MockBean
    private AdaptersDatabaseServiceImpl adaptersDatabaseService;

    @MockBean
    MspStandardRepository mspStandardRepository;

    @MockBean
    MSPActionsRepository mspActionsRepository;

    @MockBean
    MspMetaRepository mspMetaRepository;

    @MockBean
    AdaptersRepository adaptersRepository;

    @MockBean
    private ErrorMessages errorMessages;

    public StandardPK initPK(){
        StandardPK standardPK = new StandardPK();
        MSPActions mspActions = new MSPActions();
        MspMeta msp = new MspMeta();

        mspActions.setMspActionId(UUID.randomUUID());
        msp.setMspId(UUID.randomUUID());

        standardPK.setMsp(msp);
        standardPK.setVersionDataMapping("2.X.X");
        standardPK.setVersionStandard("2.X.Y");
        standardPK.setAction(mspActions);

        return standardPK;
    }
    @Test
    public void addMspStandard() {
        UUID mspStandardId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MspStandard standard = new MspStandard();
        Adapters adapters = new Adapters(UUID.randomUUID(), "generic-adapter");
        standard.setMspStandardId(mspStandardId);
        standard.setAdapter(adapters);
        standard.setId(initPK());
        Mockito.when(mspStandardRepository.save(standard)).thenReturn(standard);
        Mockito.when(mspActionsRepository.findById(standard.getId().getAction().getMspActionId())).thenReturn(Optional.of(new MSPActions()));
        Mockito.when(mspMetaRepository.findById(standard.getId().getMsp().getMspId())).thenReturn(Optional.of(new MspMeta()));
        Mockito.when(adaptersRepository.findById(standard.getAdapter().getAdapterId())).thenReturn(Optional.of(new Adapters()));


        MspStandard mspStandardToAdd = mspStandardDatabaseService.addMspStandard(standard);
        assertEquals(mspStandardToAdd, standard);
    }

    @Test
    public void updateMspStandard() {
        MspStandard standard = new MspStandard();
        UUID idMspStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        standard.setMspStandardId(idMspStandard);
        Mockito.when(mspStandardRepository.findByMspStandardId(idMspStandard)).thenReturn(standard);
        mspStandardDatabaseService.deleteMspStandard(standard.getMspStandardId());
        Mockito.when(mspStandardRepository.save(standard)).thenReturn(standard);
        MspStandard standardToUpdate = mspStandardDatabaseService.updateMspStandard(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), standard);
        assertEquals(standardToUpdate, standard);
    }

    @Test
    public void updateMspStandardException() {
        MspStandard standard = new MspStandard();
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> mspStandardDatabaseService.updateMspStandard(id, standard));
    }

    @Test
    public void deleteMspStandard() {
        MspStandard standard = new MspStandard();
        UUID idMspStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        StandardPK pk = new StandardPK();
        standard.setId(pk);
        standard.setMspStandardId(idMspStandard);
        Mockito.when(mspStandardRepository.findByMspStandardId(idMspStandard)).thenReturn(standard);
        mspStandardDatabaseService.deleteMspStandard(standard.getMspStandardId());
        verify(mspStandardRepository, times(1)).deleteById(pk);
    }

    @Test
    public void findMspStandardById() {
        UUID idMspStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MspStandard standard = new MspStandard();
        standard.setMspStandardId(idMspStandard);
        Mockito.when(mspStandardRepository.findByMspStandardId(idMspStandard)).thenReturn(standard);
        MspStandard standardToFind = mspStandardDatabaseService.findMspStandardById(idMspStandard);
        assertEquals(standardToFind, standard);
    }

    @Test
    public void getByCriteria() {
        MspStandard standard = new MspStandard();
        MspMeta msp = new MspMeta();
        MSPActions action = new MSPActions();
        UUID mspMetaId = UUID.fromString("a457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspActionsId = UUID.fromString("d457579d-02f8-4479-b97b-ffb678e3f888");
        String mspActionsName = "";
        String versionStandard = "V1";
        String versionDatamapping = "V2";
        Boolean isActive = false;
        msp.setMspId(mspMetaId);
        action.setMspActionId(mspActionsId);
        StandardPK pk = new StandardPK();
        pk.setVersionStandard(versionStandard);
        pk.setVersionDataMapping(versionDatamapping);
        pk.setMsp(msp);
        pk.setAction(action);
        standard.setId(pk);
        standard.setIsActive(isActive);
        List<MspStandard> mspStandards = new ArrayList<>();
        mspStandards.add(standard);
        Mockito.when(mspStandardRepository.findByKeyPrimary(mspMetaId, mspActionsId, mspActionsName, versionStandard, versionDatamapping, isActive)).thenReturn(mspStandards);
        List<MspStandard> allMspStandards = mspStandardDatabaseService.getByCriteria(mspMetaId, mspActionsId, mspActionsName, versionStandard, versionDatamapping, isActive);
        assertEquals(allMspStandards, mspStandards);

    }

    @Test
    public void testFindByMspStandardIdNotFound() {
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        UUID standardUuidToFind = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mspStandardDatabaseService.findMspStandardById(standardUuidToFind));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testGetAllMspStandard() {
        List<MspStandard> mspStandards = new ArrayList<>();
        Mockito.when(mspStandardRepository.findAll()).thenReturn(mspStandards);
        List<MspStandard> mspStandardsListToGet = mspStandardDatabaseService.getAllMspStandard();
        assertEquals(mspStandardsListToGet, mspStandards);
    }

    @Test
    public void deleteMspStandardKo() {
        MspStandard standard = new MspStandard();
        UUID idMspStandard = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        StandardPK pk = new StandardPK();
        standard.setId(pk);
        standard.setMspStandardId(idMspStandard);
        Mockito.when(mspStandardRepository.findByMspStandardId(idMspStandard)).thenReturn(null);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        UUID standardUuidToFind = standard.getMspStandardId();
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mspStandardDatabaseService.deleteMspStandard(standardUuidToFind));
        String actualMessage = exception.getMessage();
        assertEquals("should throw exception", actualMessage);
    }

    @Test
    public void testStandardsKo() {
        UUID idMspMeta = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mspStandardDatabaseService.getByCriteria(idMspMeta, null, null, null, null, null));
        String actualMessage = exception.getMessage();
        assertEquals("MspStandard avec (MspMeta id f457579d-02f8-4479-b97b-ffb678e3f888 ,MspActions id null,MspActions name null, Version Standard null, Version Datamapping null,Active null)", actualMessage);
    }

    @Test
    public void testAddMspStandardConflictException() {
        UUID mspStandardIDInDB = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MspStandard mspStandardInDB = new MspStandard();
        mspStandardInDB.setMspStandardId(mspStandardIDInDB);

        Optional<MspStandard> mspStandardToAdd = Optional.of(mspStandardInDB);
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(UUID.randomUUID());
        MSPActions mspActions = new MSPActions();
        mspActions.setMspActionId(UUID.randomUUID());

        StandardPK standardPK = new StandardPK();
        standardPK.setVersionStandard("2.x");
        standardPK.setVersionDataMapping("2.x");
        standardPK.setMsp(mspMeta);
        standardPK.setAction(mspActions);
        mspStandardToAdd.get().setId(standardPK);
        mspStandardInDB.setId(standardPK);

        Mockito.when(mspStandardRepository.findById(mspStandardToAdd.get().getId())).thenReturn(Optional.of(mspStandardInDB));

        MspStandard MspStandardToAddGet = mspStandardToAdd.get();
        assertThrows(ConflictException.class, () -> mspStandardDatabaseService.addMspStandard(MspStandardToAddGet));
    }

    @Test
    public void testAddMspStandardNotFoundException(){
        MspStandard mspStandard = new MspStandard();
        mspStandard.setId(initPK());
        Mockito.when(mspMetaDatabaseService.findMspMetaById(mspStandard.getId().getMsp().getMspId())).thenThrow(new NotFoundException("MSP ID INCONNU"));
        assertThrows(NotFoundException.class, () -> mspStandardDatabaseService.addMspStandard(mspStandard));
    }
}