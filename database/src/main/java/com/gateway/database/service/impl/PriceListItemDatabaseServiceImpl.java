package com.gateway.database.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.database.model.*;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.service.PriceListItemDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class PriceListItemDatabaseServiceImpl implements PriceListItemDatabaseService {

    @Autowired
    PriceListItemRepository priceListItemRepository;

    @Autowired
    MspMetaDatabaseServiceImpl mspMetaDatabaseService;

    @Override
    public List<PriceListItem> findAllPriceListItem() {
        return (List<PriceListItem>) priceListItemRepository.findAll();
    }

    @Override
    public PriceListItem findPriceListItemById(UUID id) {
        Optional<PriceListItem> priceList = priceListItemRepository.findById(id);
        return priceList.orElse(null);
    }

    @Override
    public PriceListItem createPriceListItem(PriceListItem priceListItem) {
        priceListItemRepository.save(priceListItem);
        return priceListItem;
    }

    @Override
    public List<Distance> updateDistance(Map<String, Object> updates, PriceList priceList) throws IOException {
        UUID mspId = priceList.getMspMeta().getMspId();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Distance> distanceList = objectMapper.readValue(objectMapper.writeValueAsBytes(updates.get(com.gateway.database.util.enums.PriceList.DISTANCE.getValue())), objectMapper.getTypeFactory().constructCollectionType(List.class, Distance.class));

        // manage duration
        mspMetaDatabaseService.updateDistanceItems(mspId, distanceList, priceList.getDistance());

        return priceList.getDistance();
    }


    @Override
    public List<Duration> updateDuration(Map<String, Object> updates, PriceList priceList) throws IOException {
        UUID mspId = priceList.getMspMeta().getMspId();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Duration> durationList = objectMapper.readValue(objectMapper.writeValueAsBytes(updates.get(com.gateway.database.util.enums.PriceList.DURATION.getValue())), objectMapper.getTypeFactory().constructCollectionType(List.class, Duration.class));

        // manage duration
        mspMetaDatabaseService.updateDurationItems(mspId, durationList, priceList.getDuration());

        return priceList.getDuration();
    }
}
