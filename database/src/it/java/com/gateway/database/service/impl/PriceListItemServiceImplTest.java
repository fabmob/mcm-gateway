package com.gateway.database.service.impl;


import com.gateway.database.model.*;
import com.gateway.database.repository.PriceListItemRepository;
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
public class PriceListItemServiceImplTest {

    @TestConfiguration
    public static class PriceListItemServiceTestContextConfiguration {
        @Bean
        public PriceListItemDatabaseService partnerMetaDatabaseService() {
            return new PriceListItemDatabaseServiceImpl();
        }
    }

    @MockBean
    private PriceListItemRepository priceListItemRepository;

    @MockBean
    private PartnerMetaDatabaseServiceImpl partnerMetaDatabaseService;

    @Autowired
    private PriceListItemDatabaseService priceListItemService;

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void testFindAllPriceListItem() {
        List<PriceListItem> priceListItemList = new ArrayList<>();
        Mockito.when(priceListItemRepository.findAll()).thenReturn(priceListItemList);
        List<PriceListItem> priceListItemListToFind = priceListItemService.findAllPriceListItem();
        assertEquals(priceListItemListToFind, priceListItemList);
    }

    @Test
    public void testFindPriceListItemById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987");
        PriceListItem priceListItem = new PriceListItem();
        priceListItem.setPriceListItemId(id);
        Mockito.when(priceListItemRepository.findById(id)).thenReturn(Optional.of(priceListItem));
        PriceListItem priceListItemToFind = priceListItemService.findPriceListItemById(id);
        assertEquals(priceListItemToFind, priceListItem);
    }

    @Test
    public void testCreatePriceListItem() {
        PriceListItem priceListItem = new PriceListItem();
        Mockito.when(priceListItemRepository.save(priceListItem)).thenReturn(priceListItem);
        PriceListItem priceListItemToPost = priceListItemService.createPriceListItem(priceListItem);
        assertEquals(priceListItemToPost, priceListItem);
    }

    @Test
    public void testUpdateDistance() throws IOException {
        PriceList priceList = new PriceList();
        List<Distance> distanceList = new ArrayList<>();
        priceList.setDistance(distanceList);
        PartnerMeta partnerMeta = new PartnerMeta();
        partnerMeta.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987"));
        priceList.setPartnerMeta(partnerMeta);
        Map<String, Object> updates = new HashMap<>();
        List<Distance> distanceUpdate = new ArrayList<>();

        List<Distance> distances = priceListItemService.updateDistance(updates, priceList);
        assertEquals(distances, distanceUpdate);
    }

    @Test
    public void testUpdateDuration() throws IOException {
        PriceList priceList = new PriceList();
        List<Duration> durationList = new ArrayList<>();
        priceList.setDuration(durationList);
        PartnerMeta partnerMeta = new PartnerMeta();
        partnerMeta.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f987"));
        priceList.setPartnerMeta(partnerMeta);
        Map<String, Object> updates = new HashMap<>();
        List<Duration> durationUpdate = new ArrayList<>();

        List<Duration> durations = priceListItemService.updateDuration(updates, priceList);
        assertEquals(durations, durationUpdate);
    }

}