package com.gateway.database.service.impl;

import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.model.PriceList;
import com.gateway.database.repository.PartnerMetaRepository;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.repository.PriceListRepository;
import com.gateway.database.service.PriceListDatabaseService;
import com.gateway.database.service.PriceListItemDatabaseService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class PriceListServiceImplTest {

    @TestConfiguration
    public static class PriceListServiceTestContextConfiguration {
        @Bean
        public PriceListDatabaseService priceListDatabaseService() {
            return new PriceListDatabaseServiceImpl();
        }
    }

    @Autowired
    private PriceListDatabaseService priceListService;

    @MockBean
    private PriceListRepository priceListRepository;

    @MockBean
    private PriceListItemRepository priceListItemRepository;

    @MockBean
    PriceListItemDatabaseService priceListItemService;

    @MockBean
    private PartnerMetaRepository partnerMetaRepository;

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void testFindAllPriceList() {
        List<PriceList> priceList = new ArrayList<>();

        Mockito.when(priceListRepository.findAll()).thenReturn(priceList);

        List<PriceList> priceListToFind = priceListService.findAllPriceList();
        assertEquals(priceListToFind, priceList);
    }

    @Test
    public void testFindPriceListById() {
        UUID priceListId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        PriceList priceList = new PriceList();
        priceList.setPriceListId(priceListId);

        Mockito.when(priceListRepository.findById(priceListId)).thenReturn(Optional.of(priceList));

        PriceList priceListToFind = priceListService.findPriceListById(priceListId);
        assertEquals(priceListToFind, priceList);
    }

    @Test
    public void testCreatePriceList() {
        PriceList priceList = new PriceList();
        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        priceList.setDuration(durationList);
        priceList.setDistance(distanceList);

        Mockito.when(priceListItemRepository.saveAll(distanceList)).thenReturn(distanceList);
        Mockito.when(priceListItemRepository.saveAll(durationList)).thenReturn(durationList);

        PriceList priceListCreated = priceListService.createPriceList(priceList);
        assertEquals(priceListCreated, priceList);
    }

    @Test
    public void testUpdatePriceList() throws IOException {
        PartnerMeta partnerMeta = new PartnerMeta();
        PriceList priceList1 = new PriceList();
        partnerMeta.setPriceList(priceList1);
        priceList1.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f945"));
        priceList1.setComment("comment");
        priceList1.setDistance(null);
        priceList1.setDuration(null);
        priceList1.setParkingForbiddenFee(null);
        priceList1.setOutOfBoundFee(null);

        List<Duration> durationList = new ArrayList<>();
        List<Distance> distanceList = new ArrayList<>();

        Map<String, Object> updates = new HashMap<>();
        PriceList priceList = new PriceList(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f945"),
                "Trotti price list", partnerMeta, durationList, distanceList,
                UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f922"), 5000L, 50000L);

        Map<String, Object> priceListMap = new HashMap<>();
        priceListMap.put("comment", priceList.getComment());
        priceListMap.put("outOfBoundFee", Integer.valueOf(String.valueOf(priceList.getOutOfBoundFee())));
        priceListMap.put("parkingForbiddenFee", Integer.valueOf(String.valueOf(priceList.getParkingForbiddenFee())));
        priceListMap.put("duration", priceList.getDuration());
        priceListMap.put("distance", priceList.getDistance());

        updates.put("priceList", priceListMap);

        Mockito.when(priceListRepository.save(partnerMeta.getPriceList())).thenReturn(priceList);
        Mockito.when(partnerMetaRepository.save(partnerMeta)).thenReturn(partnerMeta);
        PriceList priceListToUpdate = priceListService.updatePriceList(updates, partnerMeta);
        assertEquals(priceListToUpdate.getComment(), priceList.getComment());
    }

    @Test
    public void TestUpdatePriceListWhenPriceListIsNull() throws IOException {
        Map<String, Object> updates = new HashMap<>();
        PartnerMeta partnerMeta = new PartnerMeta();
        PriceList priceList = new PriceList();
        priceList.setPriceListId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        updates.put("priceList", priceList);

        Mockito.when(partnerMetaRepository.save(partnerMeta)).thenReturn(partnerMeta);
        PriceList priceListToUpdate = priceListService.updatePriceList(updates, partnerMeta);
        assertEquals(priceListToUpdate, priceList);
    }
}