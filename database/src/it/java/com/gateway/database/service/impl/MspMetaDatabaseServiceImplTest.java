package com.gateway.database.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PriceList;
import com.gateway.database.service.MspMetaDatabaseService;
import com.gateway.database.service.PriceListDatabaseService;
import com.gateway.database.util.enums.Msp;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import com.gateway.database.model.MspMeta;
import com.gateway.database.repository.MspMetaRepository;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.repository.PriceListRepository;


@RunWith(SpringRunner.class)
public class MspMetaDatabaseServiceImplTest {
    @TestConfiguration
    public static class MspMetaServiceImplTestContextConfiguration {
        @Bean
        public MspMetaDatabaseService mspMetaDatabaseService() {
            return new MspMetaDatabaseServiceImpl();
        }
    }

    @Autowired
    private MspMetaDatabaseService mspMetaService;

    @MockBean
    private MspMetaRepository mspMetaRepository;

    @MockBean
    private PriceListItemRepository priceListItemRepository;

    @MockBean
    private PriceListRepository priceListRepository;

    @MockBean
    private PriceListDatabaseService priceListService;

    @MockBean
    private ErrorMessages errorMessage;

    @BeforeClass
    public static void setUp() {
    }


    @Test
    public void testFindAllMspMeta() {
        // Given
        List<MspMeta> allMsp = new ArrayList<>();
        MspMeta mspMeta = new MspMeta();
        allMsp.add(mspMeta);
        // we mock the call off the database
        Mockito.when(mspMetaRepository.findAll()).thenReturn(allMsp);
        // Test (when)
        List<MspMeta> mspMetaList = mspMetaService.findAllMspMeta();
        // Then
        assertEquals(1, mspMetaList.size());
        verify(mspMetaRepository, times(1)).findAll();
    }

    @Test
    public void testFindMspMetaById() {
        // Given
        UUID idMsp = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        MspMeta msp = new MspMeta();
        msp.setMspId(idMsp);
        UUID idpriceList = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f333");
        PriceList priceList = new PriceList();
        priceList.setPriceListId(idpriceList);
        priceList.setComment("une price liste");

        List<Distance> distanceList = new ArrayList<>();
        List<Duration> durationList = new ArrayList<>();

        priceList.setDistance(distanceList);
        priceList.setDuration(durationList);
        msp.setPriceList(priceList);

        // Mock all calls to the database
        Mockito.when(mspMetaRepository.findById(idMsp)).thenReturn(Optional.of(msp));
        Mockito.when(priceListItemRepository.findAllDistancePriceListId(msp.getPriceList().getPriceListId())).thenReturn(distanceList);
        Mockito.when(priceListItemRepository.findAllDurationPriceListId(msp.getPriceList().getPriceListId())).thenReturn(durationList);

        // when I call the method that I want to test
        MspMeta mspToFind = mspMetaService.findMspMetaById(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));

        // then
        assertEquals(mspToFind, msp);
    }

    @Test
    public void testCreateMspMeta() {
        // given
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f333"));
        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();
        PriceList priceList = new PriceList();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f666"));
        priceList.setComment("une price liste 1");
        priceList.setDuration(durationList);
        priceList.setDistance(distanceList);
        mspMeta.setPriceList(priceList);
        // when
        Mockito.when(priceListItemRepository.saveAll(durationList)).thenReturn(durationList);
        Mockito.when(priceListItemRepository.saveAll(distanceList)).thenReturn(distanceList);
        //then
        MspMeta mspMetaCreated = mspMetaService.createMspMeta(mspMeta);
        assertNotEquals(mspMeta, mspMetaCreated);
    }

    @Test
    public void testRemoveMspMeta() {
        // Given
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f654");
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(mspId);

        PriceList priceListToRemove = new PriceList();
        priceListToRemove.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f665"));

        Duration duration = new Duration();
        duration.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e35432"));
        Distance distance = new Distance();
        distance.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e35430"));

        List<Duration> durationList = new ArrayList<>();
        durationList.add(duration);
        List<Distance> distanceList = new ArrayList<>();
        distanceList.add(distance);

        priceListToRemove.setDistance(distanceList);
        priceListToRemove.setDuration(durationList);
        mspMeta.setPriceList(priceListToRemove);

        // Mock
        Mockito.when(mspMetaRepository.findById(mspId)).thenReturn(Optional.of(mspMeta));
        doNothing().when(priceListRepository).removePricelistById(mspMeta.getPriceList().getPriceListId());
        // When I call the method that I want to test
        mspMetaService.removeMspMeta(mspId);
        // Then verify that it's invoked once (for testing void methods)
        verify(mspMetaRepository, times(1)).deleteById(mspId);
    }

    @Test
    public void testupdateMspMeta() {
        // Given
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987"));

        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();

        PriceList priceList = new PriceList();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f661"));
        priceList.setComment("une price list ");
        priceList.setDuration(durationList);
        priceList.setDistance(distanceList);
        mspMeta.setPriceList(priceList);

        // Mock
        Mockito.when(mspMetaRepository.findById(mspId)).thenReturn(Optional.of(mspMeta));
        Mockito.when(mspMetaRepository.save(mspMeta)).thenReturn(mspMeta);
        // When
        mspMetaService.updateMspMeta(mspId, mspMeta);
        //Then
        verify(mspMetaRepository, times(2)).save(mspMeta);
    }

    @Test
    public void testUpdateDurationItems() {
        // Given
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(mspId);
        Duration duration = new Duration();
        duration.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f000"));
        List<Duration> durationsList = new ArrayList<>();
        durationsList.add(duration);
        Duration durationUpdate = new Duration();
        durationUpdate.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f002"));
        List<Duration> durationsUpdate = new ArrayList<>();
        durationsUpdate.add(durationUpdate);

        // Mock
        Mockito.when(mspMetaRepository.findById(mspId)).thenReturn(Optional.of(mspMeta));
        Mockito.when(priceListItemRepository.save(durationUpdate)).thenReturn(durationUpdate);
        doNothing().when(priceListItemRepository).removePricelistId(duration.getPriceListItemId());

        // When
        mspMetaService.updateDurationItems(mspId, durationsUpdate, durationsList);
        //Then
        verify(priceListItemRepository, times(1)).save(durationUpdate);
    }

    @Test
    public void testUpdateDistanceItems() {
        //Given
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(mspId);
        Distance distance = new Distance();
        distance.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f000"));
        List<Distance> distancesList = new ArrayList<>();
        distancesList.add(distance);
        Distance distanceUpdate = new Distance();
        distanceUpdate.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f002"));
        List<Distance> distancesUpdate = new ArrayList<>();
        distancesUpdate.add(distanceUpdate);
        // Mock
        Mockito.when(mspMetaRepository.findById(mspId)).thenReturn(Optional.of(mspMeta));
        Mockito.when(priceListItemRepository.save(distanceUpdate)).thenReturn(distanceUpdate);
        doNothing().when(priceListItemRepository).removePricelistId(distance.getPriceListItemId());
        // When
        mspMetaService.updateDistanceItems(mspId, distancesUpdate, distancesList);
        //Then
        verify(priceListItemRepository, times(1)).save(distanceUpdate);
    }

    @Test
    public void testUpdateMspMeta() throws IOException {
        Map<String, Object> updates = new HashMap<>();
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        // The msp supposed in the database
        MspMeta mspMeta = new MspMeta();
        mspMeta.setMspId(mspId);

        MspMeta msp = new MspMeta();
        msp.setMspId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f944"));
        msp.setOperator("Kapten");
        msp.setName("Kapten");
        msp.setType("VTC");
        msp.setUrl("https://www.kapten.com/fr/index.html");
        msp.setUrlWebview(false);
        msp.setLogoUrl("@MAAS_API_DIRECTORY_ICON@/icon_kapten@3x.png");
        msp.setLogoFormat("PNG");
        msp.setPrimaryColor("#0E5980");
        msp.setSecondaryColor("#0E5980");
        msp.setIsEnabled(true);
        msp.setHasHold(true);
        msp.setHasStation(true);
        msp.setHasStationStatus(true);
        msp.setHasVehicle(true);
        msp.setHasSpeedLimitZone(true);
        msp.setHasOperatingZone(true);
        msp.setHasNoParkingZone(true);
        msp.setHasSpeedLimitZone(true);
        msp.setHasPrefParkingZone(true);
        msp.setHasParking(true);
        msp.setHasHold(true);

        updates.put(String.valueOf(Msp.NAME), msp.getName());
        updates.put(String.valueOf(Msp.OPERATOR), msp.getOperator());
        updates.put(String.valueOf(Msp.TYPE), msp.getType());
        updates.put(String.valueOf(Msp.URL), msp.getUrl());
        updates.put(String.valueOf(Msp.URLWEBVIEW), msp.getUrlWebview());
        updates.put(String.valueOf(Msp.LOGOURL), msp.getLogoUrl());
        updates.put(String.valueOf(Msp.LOGOFORMAT), msp.getLogoUrl());
        updates.put(String.valueOf(Msp.PRIMARYCOLOR), msp.getPrimaryColor());
        updates.put(String.valueOf(Msp.SECONDARYCOLOR), msp.getSecondaryColor());
        updates.put(String.valueOf(Msp.ISENABLED), msp.getIsEnabled());
        updates.put(String.valueOf(Msp.HASVEHICLE), msp.getHasVehicle());
        updates.put(String.valueOf(Msp.HASSTATION), msp.getHasStation());
        updates.put(String.valueOf(Msp.HASSTATIONSTATUS), msp.getHasStationStatus());
        updates.put(String.valueOf(Msp.HASOPERATINGZONE), msp.getHasOperatingZone());
        updates.put(String.valueOf(Msp.HASNOPARKINGZONE), msp.getHasNoParkingZone());
        updates.put(String.valueOf(Msp.HASPREFPARKINGZONE), msp.getHasPrefParkingZone());
        updates.put(String.valueOf(Msp.HASSPEEDLIMITZONE), msp.getHasSpeedLimitZone());
        updates.put(String.valueOf(Msp.HASPARKING), msp.getHasParking());
        updates.put(String.valueOf(Msp.HASHOLD), msp.getHasHold());
        updates.put(String.valueOf(Msp.PRICELIST), msp.getPriceList());

        // Mock
        Mockito.when(mspMetaRepository.findById(mspId)).thenReturn(Optional.of(mspMeta));
        Mockito.when(mspMetaRepository.save(msp)).thenReturn(msp);

        // When
        MspMeta mspResult = mspMetaService.updateMspMeta(updates, mspId);
        // Then
        assertNotEquals(msp, mspResult);
    }


}