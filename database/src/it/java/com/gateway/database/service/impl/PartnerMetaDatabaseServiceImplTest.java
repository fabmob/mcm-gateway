package com.gateway.database.service.impl;

import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.TypeEnum;
import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.model.PriceList;
import com.gateway.database.repository.PartnerMetaRepository;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.repository.PriceListRepository;
import com.gateway.database.service.PartnerMetaDatabaseService;
import com.gateway.database.service.PriceListDatabaseService;
import com.gateway.database.util.enums.Partner;
import org.junit.BeforeClass;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class PartnerMetaDatabaseServiceImplTest {
    @TestConfiguration
    public static class PartnerMetaServiceImplTestContextConfiguration {
        @Bean
        public PartnerMetaDatabaseService PartnerMetaDatabaseService() {
            return new PartnerMetaDatabaseServiceImpl();
        }
    }

    @Autowired
    private PartnerMetaDatabaseService PartnerMetaService;

    @MockBean
    private PartnerMetaRepository partnerMetaRepository;

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
    public void testFindAllPartnerMeta() {
        // Given
        List<PartnerMeta> allPartner = new ArrayList<>();
        PartnerMeta PartnerMeta = new PartnerMeta();
        allPartner.add(PartnerMeta);
        // we mock the call off the database
        Mockito.when(partnerMetaRepository.findAll()).thenReturn(allPartner);
        // Test (when)
        List<PartnerMeta> PartnerMetaList = PartnerMetaService.findAllPartnerMeta();
        // Then
        assertEquals(1, PartnerMetaList.size());
        verify(partnerMetaRepository, times(1)).findAll();
    }

    @Test
    public void testFindPartnerMetaById() {
        // Given
        UUID idPartner = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PartnerMeta partner = new PartnerMeta();
        partner.setPartnerId(idPartner);
        UUID idpriceList = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f333");
        PriceList priceList = new PriceList();
        priceList.setPriceListId(idpriceList);
        priceList.setComment("une price liste");

        List<Distance> distanceList = new ArrayList<>();
        List<Duration> durationList = new ArrayList<>();

        priceList.setDistance(distanceList);
        priceList.setDuration(durationList);
        partner.setPriceList(priceList);

        // Mock all calls to the database
        Mockito.when(partnerMetaRepository.findById(idPartner)).thenReturn(Optional.of(partner));
        Mockito.when(priceListItemRepository.findAllDistancePriceListId(partner.getPriceList().getPriceListId())).thenReturn(distanceList);
        Mockito.when(priceListItemRepository.findAllDurationPriceListId(partner.getPriceList().getPriceListId())).thenReturn(durationList);

        // when I call the method that I want to test
        PartnerMeta partnerToFind = PartnerMetaService.findPartnerMetaById(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));

        // then
        assertEquals(partnerToFind, partner);
    }

    @Test
    public void testCreatePartnerMeta() {
        // given
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f333"));
        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();
        PriceList priceList = new PriceList();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f666"));
        priceList.setComment("une price liste 1");
        priceList.setDuration(durationList);
        priceList.setDistance(distanceList);
        PartnerMeta.setPriceList(priceList);
        PartnerMeta.setPartnerType(PartnerTypeEnum.MSP.toString());
        // when
        Mockito.when(priceListItemRepository.saveAll(durationList)).thenReturn(durationList);
        Mockito.when(priceListItemRepository.saveAll(distanceList)).thenReturn(distanceList);
        //then
        PartnerMeta PartnerMetaCreated = PartnerMetaService.createPartnerMeta(PartnerMeta);
        assertNotEquals(PartnerMeta, PartnerMetaCreated);
    }

    @Test
    public void testRemovePartnerMeta() {
        // Given
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f654");
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(partnerId);

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
        PartnerMeta.setPriceList(priceListToRemove);

        // Mock
        Mockito.when(partnerMetaRepository.findById(partnerId)).thenReturn(Optional.of(PartnerMeta));
        doNothing().when(priceListRepository).removePricelistById(PartnerMeta.getPriceList().getPriceListId());
        // When I call the method that I want to test
        PartnerMetaService.removePartnerMeta(partnerId);
        // Then verify that it's invoked once (for testing void methods)
        verify(partnerMetaRepository, times(1)).deleteById(partnerId);
    }

    @Test
    public void testupdatePartnerMeta() {
        // Given
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987"));

        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();

        PriceList priceList = new PriceList();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f661"));
        priceList.setComment("une price list ");
        priceList.setDuration(durationList);
        priceList.setDistance(distanceList);
        PartnerMeta.setPriceList(priceList);

        // Mock
        Mockito.when(partnerMetaRepository.findById(partnerId)).thenReturn(Optional.of(PartnerMeta));
        Mockito.when(partnerMetaRepository.save(PartnerMeta)).thenReturn(PartnerMeta);
        // When
        PartnerMetaService.updatePartnerMeta(partnerId, PartnerMeta);
        //Then
        verify(partnerMetaRepository, times(2)).save(PartnerMeta);
    }

    @Test
    public void testUpdateDurationItems() {
        // Given
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(partnerId);
        Duration duration = new Duration();
        duration.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f000"));
        List<Duration> durationsList = new ArrayList<>();
        durationsList.add(duration);
        Duration durationUpdate = new Duration();
        durationUpdate.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f002"));
        List<Duration> durationsUpdate = new ArrayList<>();
        durationsUpdate.add(durationUpdate);

        // Mock
        Mockito.when(partnerMetaRepository.findById(partnerId)).thenReturn(Optional.of(PartnerMeta));
        Mockito.when(priceListItemRepository.save(durationUpdate)).thenReturn(durationUpdate);
        doNothing().when(priceListItemRepository).removePricelistId(duration.getPriceListItemId());

        // When
        PartnerMetaService.updateDurationItems(partnerId, durationsUpdate, durationsList);
        //Then
        verify(priceListItemRepository, times(1)).save(durationUpdate);
    }

    @Test
    public void testUpdateDistanceItems() {
        //Given
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(partnerId);
        Distance distance = new Distance();
        distance.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f000"));
        List<Distance> distancesList = new ArrayList<>();
        distancesList.add(distance);
        Distance distanceUpdate = new Distance();
        distanceUpdate.setPriceListItemId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f002"));
        List<Distance> distancesUpdate = new ArrayList<>();
        distancesUpdate.add(distanceUpdate);
        // Mock
        Mockito.when(partnerMetaRepository.findById(partnerId)).thenReturn(Optional.of(PartnerMeta));
        Mockito.when(priceListItemRepository.save(distanceUpdate)).thenReturn(distanceUpdate);
        doNothing().when(priceListItemRepository).removePricelistId(distance.getPriceListItemId());
        // When
        PartnerMetaService.updateDistanceItems(partnerId, distancesUpdate, distancesList);
        //Then
        verify(priceListItemRepository, times(1)).save(distanceUpdate);
    }

    @Test
    public void testUpdatePartnerMeta() {
        Map<String, Object> updates = new HashMap<>();
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        // The partner supposed in the database
        PartnerMeta PartnerMeta = new PartnerMeta();
        PartnerMeta.setPartnerId(partnerId);

        PartnerMeta partner = new PartnerMeta();
        partner.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f944"));
        partner.setOperator("Kapten");
        partner.setName("Kapten");
        partner.setType(TypeEnum.CARPOOLING.toString());
        partner.setUrl("https://www.kapten.com/fr/index.html");
        partner.setUrlWebview(false);
        partner.setLogoUrl("@MAAS_API_DIRECTORY_ICON@/icon_kapten@3x.png");
        partner.setLogoFormat("PNG");
        partner.setPrimaryColor("#0E5980");
        partner.setSecondaryColor("#0E5980");
        partner.setIsEnabled(true);
        partner.setHasHold(true);
        partner.setHasStation(true);
        partner.setHasStationStatus(true);
        partner.setHasVehicle(true);
        partner.setHasSpeedLimitZone(true);
        partner.setHasOperatingZone(true);
        partner.setHasNoParkingZone(true);
        partner.setHasSpeedLimitZone(true);
        partner.setHasPrefParkingZone(true);
        partner.setHasParking(true);
        partner.setHasHold(true);

        updates.put(String.valueOf(Partner.NAME), partner.getName());
        updates.put(String.valueOf(Partner.OPERATOR), partner.getOperator());
        updates.put(String.valueOf(Partner.TYPE), partner.getType());
        updates.put(String.valueOf(Partner.URL), partner.getUrl());
        updates.put(String.valueOf(Partner.URLWEBVIEW), partner.getUrlWebview());
        updates.put(String.valueOf(Partner.LOGOURL), partner.getLogoUrl());
        updates.put(String.valueOf(Partner.LOGOFORMAT), partner.getLogoUrl());
        updates.put(String.valueOf(Partner.PRIMARYCOLOR), partner.getPrimaryColor());
        updates.put(String.valueOf(Partner.SECONDARYCOLOR), partner.getSecondaryColor());
        updates.put(String.valueOf(Partner.ISENABLED), partner.getIsEnabled());
        updates.put(String.valueOf(Partner.HASVEHICLE), partner.getHasVehicle());
        updates.put(String.valueOf(Partner.HASSTATION), partner.getHasStation());
        updates.put(String.valueOf(Partner.HASSTATIONSTATUS), partner.getHasStationStatus());
        updates.put(String.valueOf(Partner.HASOPERATINGZONE), partner.getHasOperatingZone());
        updates.put(String.valueOf(Partner.HASNOPARKINGZONE), partner.getHasNoParkingZone());
        updates.put(String.valueOf(Partner.HASPREFPARKINGZONE), partner.getHasPrefParkingZone());
        updates.put(String.valueOf(Partner.HASSPEEDLIMITZONE), partner.getHasSpeedLimitZone());
        updates.put(String.valueOf(Partner.HASPARKING), partner.getHasParking());
        updates.put(String.valueOf(Partner.HASHOLD), partner.getHasHold());
        updates.put(String.valueOf(Partner.PRICELIST), partner.getPriceList());

        // Mock
        when(partnerMetaRepository.findById(partnerId)).thenReturn(Optional.of(PartnerMeta));
        when(partnerMetaRepository.save(partner)).thenReturn(partner);

        // When
        PartnerMeta mspResult = PartnerMetaService.updatePartnerMeta(updates, partnerId);
        // Then
        assertNotEquals(partner, mspResult);
    }


}