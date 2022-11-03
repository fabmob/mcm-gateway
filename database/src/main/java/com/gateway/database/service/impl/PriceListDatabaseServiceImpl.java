package com.gateway.database.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.model.PriceList;
import com.gateway.database.repository.PartnerMetaRepository;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.repository.PriceListRepository;
import com.gateway.database.service.PriceListDatabaseService;
import com.gateway.database.service.PriceListItemDatabaseService;
import com.gateway.database.util.enums.Partner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PriceListDatabaseServiceImpl implements PriceListDatabaseService {

    @Autowired
    PriceListRepository priceListRepository;

    @Autowired
    PartnerMetaRepository partnerMetaRepository;

    @Autowired
    PriceListItemDatabaseService priceListItemService;

    @Autowired
    PriceListItemRepository priceListItemRepository;

    @Override
    public List<PriceList> findAllPriceList() {
        return (List<PriceList>) priceListRepository.findAll();
    }

    @Override
    public PriceList findPriceListById(UUID id) {
        Optional<PriceList> priceList = priceListRepository.findById(id);
        return priceList.orElse(null);
    }

    @Override
    public PriceList createPriceList(PriceList priceList) {
        if (priceList.getDuration() != null) {
            List<Duration> durationList = priceList.getDuration();
            durationList.forEach(duration -> duration.setPriceListForDuration(priceList));
            priceListItemRepository.saveAll(durationList);
            priceList.setDuration(durationList);
        }
        if (priceList.getDistance() != null) {
            List<Distance> distanceList = priceList.getDistance();
            distanceList.forEach(distance -> distance.setPriceListForDistance(priceList));
            priceListItemRepository.saveAll(distanceList);
            priceList.setDistance(distanceList);
        }

        priceListRepository.save(priceList);
        return priceList;
    }

    @Override
    public PriceList updatePriceList(Map<String, Object> updates, PartnerMeta partnerMeta) throws IOException {
        PriceList priceList = partnerMeta.getPriceList();
        PriceList priceListFinal;
        if (priceList == null && updates.get(Partner.PRICELIST.getValue()) != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            priceListFinal = createPriceList(objectMapper.readValue(objectMapper.writeValueAsBytes(updates.get(Partner.PRICELIST.getValue())), PriceList.class));
        } else {
            priceListFinal = priceList;
            Map<String, Object> priceListMap = (Map<String, Object>) updates.get(Partner.PRICELIST.getValue());
            priceListMap.forEach(
                    (change, value) -> {
                        if (value != null) {
                            switch (com.gateway.database.util.enums.PriceList.valueOf(change.toUpperCase())) {
                                case COMMENT:
                                    priceListFinal.setComment(String.valueOf(value));
                                    break;
                                case OUTOFBOUNDFEE:
                                    priceList.setOutOfBoundFee(Long.valueOf((Integer) value));
                                    break;
                                case PARKINGFORBIDDENFEE:
                                    priceList.setParkingForbiddenFee(Long.valueOf((Integer) value));
                                    break;
                                case DURATION:
                                    if (priceList != null) {
                                        try {
                                            priceList.setDuration(priceListItemService.updateDuration(priceListMap, priceList));
                                        } catch (IOException e) {
                                            log.error(e.getMessage(), e);
                                        }
                                    }
                                    break;
                                case DISTANCE:
                                    if (priceList != null) {
                                        try {
                                            priceList.setDistance(priceListItemService.updateDistance(priceListMap, priceList));
                                        } catch (IOException e) {
                                            log.error(e.getMessage(), e);
                                        }
                                    }
                                    break;
                            }
                        }
                    }
            );
        }
        partnerMeta.setPriceList(priceListFinal);
        if (partnerMeta.getPriceList() != null) {
            priceListRepository.save(partnerMeta.getPriceList());
            partnerMetaRepository.save(partnerMeta);
        }
        return partnerMeta.getPriceList();
    }

}
