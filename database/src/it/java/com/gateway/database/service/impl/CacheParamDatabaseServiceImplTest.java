package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.CacheParam;
import com.gateway.database.model.CacheParamPK;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.repository.CacheParamRepository;
import com.gateway.database.service.CacheParamDatabaseService;
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
public class CacheParamDatabaseServiceImplTest {
    @TestConfiguration
    public static class CacheParamDatabaseServiceImplTestContextConfiguration {
        @Bean
        public CacheParamDatabaseService cacheParamDatabaseService(CacheParamRepository cacheParamRepository) {
            return new CacheParamDatabaseServiceImpl(cacheParamRepository);
        }
    }

    @MockBean
    private CacheParamRepository cacheParamRepository;

    @Autowired
    private CacheParamDatabaseService cacheParamDatabaseService;

    @MockBean
    private ErrorMessages errorMessages;

    public CacheParam initCacheParam() {
        CacheParam cacheParam = new CacheParam();
        CacheParamPK cacheParamPK = new CacheParamPK();
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        cacheParam.setCacheParamId(UUID.randomUUID());
        PartnerMeta partnerMeta = new PartnerMeta();
        partnerMeta.setPartnerId(partnerId);
        cacheParam.setSoftTTL(333);
        cacheParam.setHardTTL(999);
        cacheParamPK.setActionType("STATION_SEARCH");
        cacheParamPK.setPartner(partnerMeta);
        cacheParam.setCacheParamPK(cacheParamPK);
        cacheParam.setRefreshCacheDelay(666);
        return cacheParam;
    }




    @Test
    public void testAddCacheParamAndInternalException() {
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.save(cacheParam)).thenReturn(cacheParam);
        CacheParam cacheParamToAdd = cacheParamDatabaseService.addCacheParam(cacheParam);
        assertEquals(cacheParam, cacheParamToAdd);

        //Exception when softTTL>hardTTL
        cacheParam.setSoftTTL(1200);
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.addCacheParam(cacheParam));

        //Exception when refreshCacheDelay < 5
        cacheParam.setRefreshCacheDelay(2);
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.addCacheParam(cacheParam));
    }

    @Test
    public void testAddCacheParamNullPointerException(){
        CacheParam cacheParam = new CacheParam();
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.addCacheParam(cacheParam));
    }

    @Test
    public void testAddCacheParamConflictException() {
        //Exception when data already exists in DB
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.findById(cacheParam.getCacheParamPK())).thenReturn(Optional.of(cacheParam));
        assertThrows(ConflictException.class, () -> cacheParamDatabaseService.addCacheParam(cacheParam));
    }

    @Test
    public void testAddCacheParamNull() {
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.findById(cacheParam.getCacheParamPK())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> cacheParamDatabaseService.addCacheParam(cacheParam));
    }

    @Test
    public void testGetAllCacheParams() {
        List<CacheParam> cacheParamList = new ArrayList<>();
        Mockito.when(cacheParamRepository.findAll()).thenReturn(cacheParamList);
        List<CacheParam> cacheParamListToGet = cacheParamDatabaseService.getAllCacheParams();
        assertEquals(cacheParamListToGet, cacheParamList);
    }


    @Test
    public void testDeleteCacheParamByPK() {
        CacheParam cacheParam = initCacheParam();
        cacheParamDatabaseService.deleteCacheParam(cacheParam.getCacheParamPK());
        verify(cacheParamRepository, times(1)).deleteById(cacheParam.getCacheParamPK());
    }

    @Test public void testDeleteCacheParamByID(){
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.findByCacheParamId(cacheParam.getCacheParamId())).thenReturn(cacheParam);
        cacheParamDatabaseService.deleteCacheParam(cacheParam.getCacheParamId());
        verify(cacheParamRepository, times(1)).deleteById(cacheParam.getCacheParamPK());
    }

    @Test
    public void testFindCacheParamByPK() {
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.findById(cacheParam.getCacheParamPK())).thenReturn(Optional.of(cacheParam));
        CacheParam cacheParamToFind = cacheParamDatabaseService.findCacheParamByPK(cacheParam.getCacheParamPK());
        assertEquals(cacheParamToFind, cacheParam);
    }

    @Test
    public void testFindCacheParamByID() {
        CacheParam cacheParam = initCacheParam();
        Mockito.when(cacheParamRepository.findByCacheParamId(cacheParam.getCacheParamId())).thenReturn(cacheParam);
        CacheParam cacheParamToFind = cacheParamDatabaseService.findCacheParamByID(cacheParam.getCacheParamId());
        assertEquals(cacheParamToFind, cacheParam);
    }


    @Test
    public void testConstructorCacheParamDatabaseServiceImpl() {
        CacheParamDatabaseServiceImpl cacheParamDatabaseServiceImpl =
                new CacheParamDatabaseServiceImpl(cacheParamRepository);
        assertEquals(cacheParamRepository, cacheParamDatabaseServiceImpl.getCacheParamRepository());

    }

    @Test
    public void testUpdateCacheParamByPK() {
        CacheParam cacheParam = initCacheParam();
        CacheParam cacheParamToUpdate = initCacheParam();

        cacheParamToUpdate.setRefreshCacheDelay(10);
        cacheParamToUpdate.setHardTTL(800);
        cacheParamToUpdate.setSoftTTL(400);

        Mockito.when(cacheParamRepository.findById(cacheParam.getCacheParamPK())).thenReturn(Optional.of(cacheParam));
        Mockito.when(cacheParamRepository.save(cacheParam)).thenReturn(cacheParam);

        CacheParam cacheParamUpdated = cacheParamDatabaseService.updateCacheParam(cacheParam.getCacheParamPK(), cacheParamToUpdate);

        assertEquals(cacheParamToUpdate, cacheParamUpdated);

        //Exception when softTTL>hardTTL
        cacheParam.setSoftTTL(1200);
        CacheParamPK cacheParamPK = cacheParam.getCacheParamPK();
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.updateCacheParam(cacheParamPK, cacheParam));

        //Exception when refreshCacheDelay < 5
        cacheParam.setRefreshCacheDelay(2);
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.updateCacheParam(cacheParamPK,cacheParam));
    }

    @Test
    public void testUpdateCacheParamByID() {
        CacheParam cacheParam = initCacheParam();
        CacheParam cacheParamToUpdate = initCacheParam();

        cacheParamToUpdate.setRefreshCacheDelay(10);
        cacheParamToUpdate.setHardTTL(800);
        cacheParamToUpdate.setSoftTTL(400);

        Mockito.when(cacheParamRepository.findByCacheParamId(cacheParam.getCacheParamId())).thenReturn(cacheParam);
        Mockito.when(cacheParamRepository.save(cacheParam)).thenReturn(cacheParam);

        CacheParam cacheParamUpdated = cacheParamDatabaseService.updateCacheParam(cacheParam.getCacheParamId(), cacheParamToUpdate);

        assertEquals(cacheParamToUpdate, cacheParamUpdated);

        //Exception when softTTL>hardTTL
        cacheParam.setSoftTTL(1200);
        UUID cacheParamId = cacheParam.getCacheParamId();
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.updateCacheParam(cacheParamId, cacheParam));

        //Exception when refreshCacheDelay < 5
        cacheParam.setRefreshCacheDelay(2);
        assertThrows(InternalException.class, () -> cacheParamDatabaseService.updateCacheParam(cacheParamId,cacheParam));
    }

    @Test
    public void testGetByCriteria(){
        List<CacheParam> cacheParamList = new ArrayList<>();
        cacheParamList.add(this.initCacheParam());
        Mockito.when(cacheParamRepository.findByCriteria("STATION_SEARCH",null)).thenReturn(cacheParamList);
        List<CacheParam> cacheParamListToFind = cacheParamDatabaseService.getAllCacheParamByCriteria(null,"STATION_SEARCH");

        assertEquals(cacheParamListToFind, cacheParamList);
    }

    @Test
    public void testGetByCriteriaNotFoundException(){
        Mockito.when(cacheParamRepository.findByCriteria("STATION_SEARCH",null)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> cacheParamDatabaseService.getAllCacheParamByCriteria(null, "STATION_SEARCH"));

    }

}
