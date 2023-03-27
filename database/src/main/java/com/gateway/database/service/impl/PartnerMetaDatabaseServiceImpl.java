package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static com.gateway.commonapi.constants.AttributeDict.*;
import static com.gateway.database.util.constant.DataMessageDict.FIRST_PLACEHOLDER;
import static com.gateway.database.util.constant.DataMessageDict.PARTNER_META_WITH_ID_IS_NOT_FOUND;

@Slf4j
@Service
public class PartnerMetaDatabaseServiceImpl implements PartnerMetaDatabaseService {

    @Autowired
    private PartnerMetaRepository partnerMetaRepository;

    @Autowired
    private PriceListItemRepository priceListItemRepository;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListDatabaseService priceListService;

    @Autowired
    private ErrorMessages errorMessage;

    @Override
    public List<PartnerMeta> findAllByExample(PartnerMeta partnerMetaExample) {
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher(PARTNER_TYPE_ATTRIBUTE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(TYPE_ATTRIBUTE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(NAME_ATTRIBUTE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(OPERATOR_ATTRIBUTE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<PartnerMeta> example = Example.of(partnerMetaExample, caseInsensitiveExampleMatcher);

        return (List<PartnerMeta>) partnerMetaRepository.findAll(example);
    }

    @Override
    public List<PartnerMeta> findAllPartnerMeta() {
        return (List<PartnerMeta>) partnerMetaRepository.findAll();
    }

    @Override
    public PartnerMeta findPartnerMetaById(UUID partnerId) {

        PartnerMeta partner = partnerMetaRepository.findById(partnerId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_META_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, partnerId.toString()))));

        if (null != partner.getPriceList()) {
            // retrieve both list of distance and duration
            List<Distance> distanceList = priceListItemRepository
                    .findAllDistancePriceListId(partner.getPriceList().getPriceListId());
            List<Duration> durationList = priceListItemRepository
                    .findAllDurationPriceListId(partner.getPriceList().getPriceListId());

            // attach them to the priceLists corresponding
            partner.getPriceList().setDuration(durationList);
            partner.getPriceList().setDistance(distanceList);
            if (log.isDebugEnabled()) {
                log.debug("Partner ID {}", partner.getPartnerId());
                if (partner.getPriceList() != null) {
                    log.debug("\tPricelist ID {}", partner.getPriceList().getPriceListId());
                    log.debug("\tPricelist duration size {}", partner.getPriceList().getDuration().size());
                    log.debug("\tPricelist distance size {}", partner.getPriceList().getDistance().size());
                }
            }
        }
        return partner;

    }


    @Override
    public PartnerMeta createPartnerMeta(PartnerMeta partnerMeta) {

        if (null != partnerMeta.getPriceList()) {

            // we save the priceList Without items as first we need element to be stored in
            // order to have the primary key
            PriceList createdPriceList = priceListService.createPriceList(partnerMeta.getPriceList());
            // now we save the partnerMeta object
            partnerMeta.setPriceList(createdPriceList);
        }
        return partnerMetaRepository.save(partnerMeta);
    }

    @Override
    public void removePartnerMeta(UUID id) {
        PartnerMeta partner = findPartnerMetaById(id);
        PriceList priceListToRemove = partner.getPriceList();
        partnerMetaRepository.deleteById(id);
        if (null != priceListToRemove) {
            partner.getPriceList().getDuration()
                    .forEach(duration -> priceListItemRepository.removePricelistId(duration.getPriceListItemId()));
            partner.getPriceList().getDistance()
                    .forEach(distance -> priceListItemRepository.removePricelistId(distance.getPriceListItemId()));
            partner.getPriceList().setPartnerMeta(null);
            // remove the PartnerMeta
            priceListRepository.removePricelistById(priceListToRemove.getPriceListId());
        }
    }

    /**
     * Update full partnerMeta entity
     *
     * @param id          Identifier of the partnerMeta
     * @param partnerMeta partnerMeta object that represents the entity to update
     */
    @Override
    public void updatePartnerMeta(UUID id, PartnerMeta partnerMeta) {
        partnerMeta.setPartnerId(id);
        if (null != partnerMeta.getPriceList()) {

            if (findPartnerMetaById(id).getPriceList() == null) {
                // creation of the pricelist that does not exist yet in database
                partnerMeta.setPriceList(priceListService.createPriceList(partnerMeta.getPriceList()));
                partnerMetaRepository.save(partnerMeta);
            }
            PriceList priceListUpdate = partnerMeta.getPriceList();
            priceListUpdate.setPriceListId(findPartnerMetaById(id).getPriceList().getPriceListId());

            List<Duration> durationUpdate = priceListUpdate.getDuration();
            List<Distance> distanceUpdate = priceListUpdate.getDistance();
            try {
                // if some element has uuid on pricelistItem, get it. For other elements remove
                // and create
                List<Duration> duration = findPartnerMetaById(id).getPriceList().getDuration();
                List<Distance> distance = findPartnerMetaById(id).getPriceList().getDistance();

                // manage duration
                updateDurationItems(id, durationUpdate, duration);

                // manage Distance
                updateDistanceItems(id, distanceUpdate, distance);

                partnerMeta.setPriceList(priceListUpdate);
                partnerMetaRepository.save(partnerMeta);
            } catch (Exception e) {
                log.error("Error updating object partnerMeta with id : {}", id, e);
            }
        }
        partnerMetaRepository.save(partnerMeta);
    }

    /**
     * Compare the list of duration from dto to update with the list of duration in
     * database
     *
     * @param partnerId      id of the partner
     * @param durationUpdate list of duration from dto
     * @param duration       list of duration from database
     */
    @Override
    public void updateDurationItems(UUID partnerId, List<Duration> durationUpdate, List<Duration> duration) {
        Set<UUID> durationsFromDatabase = new HashSet<>();
        PriceList pricelistElement = findPartnerMetaById(partnerId).getPriceList();

        duration.forEach(durationItem -> {
            durationsFromDatabase.add(durationItem.getPriceListItemId());
            log.debug("Duration element found in database {}", durationItem.getPriceListItemId());
        });

        durationUpdate.forEach(durationItem -> durationsFromDatabase.remove(durationItem.getPriceListItemId()));

        // durationsFromDatabase contains all elements to remove
        durationsFromDatabase.forEach(uuid -> {
            log.debug("removing duration with pricelistItemId {}", uuid);
            priceListItemRepository.removePricelistId(uuid);
        });
        durationsFromDatabase.clear();

        log.debug("size of durationToUpdate {}", durationUpdate.size());
        log.debug("size of duration with uuid in database {}", durationsFromDatabase.size());
        durationUpdate.forEach(durationItem -> {
            // case with uuid
            if (durationItem.getPriceListItemId() != null) {
                durationItem.setPriceListForDuration(pricelistElement);
                // update the item
                priceListItemRepository.save(durationItem);
                // case with item to create
            } else {
                Duration newPricelistItem = priceListItemRepository.save(durationItem);
                durationItem.setPriceListItemId(newPricelistItem.getPriceListItemId());
            }
            durationItem.setPriceListForDuration(pricelistElement);
        });
    }

    /**
     * Compare the list of distance from dto to update with the list of distance in
     * database
     *
     * @param partnerId      id of the partner
     * @param distanceUpdate list of distance from dto
     * @param distance       list of distance from database
     */
    @Override
    public void updateDistanceItems(UUID partnerId, List<Distance> distanceUpdate, List<Distance> distance) {
        Set<UUID> distancesFromDatabase = new HashSet<>();
        PriceList pricelistElement = findPartnerMetaById(partnerId).getPriceList();

        distance.forEach(distanceItem -> {
            distancesFromDatabase.add(distanceItem.getPriceListItemId());
            log.debug("Distance element found in database {}", distanceItem.getPriceListItemId());
        });

        distanceUpdate.forEach(distanceItem -> distancesFromDatabase.remove(distanceItem.getPriceListItemId()));

        // distancesFromDatabase contains all elements to remove
        distancesFromDatabase.forEach(uuid -> {
            log.debug("removing distance with pricelistItemId {}", uuid);
            priceListItemRepository.removePricelistId(uuid);
        });
        distancesFromDatabase.clear();

        log.debug("size of distanceToUpdate {}", distanceUpdate.size());
        log.debug("size of distance with uuid in database {}", distancesFromDatabase.size());
        distanceUpdate.forEach(distanceItem -> {
            // case with uuid
            if (distanceItem.getPriceListItemId() != null) {
                distanceItem.setPriceListForDistance(pricelistElement);
                // update the item
                priceListItemRepository.save(distanceItem);
                // case with item to create
            } else {
                Distance newPricelistItem = priceListItemRepository.save(distanceItem);
                distanceItem.setPriceListItemId(newPricelistItem.getPriceListItemId());
            }
            distanceItem.setPriceListForDistance(pricelistElement);
        });
    }

    @Override
    public PartnerMeta updatePartnerMeta(Map<String, Object> updates, UUID partnerId) {
        PartnerMeta partnerMeta = this.findPartnerMetaById(partnerId);

        updates.forEach((change, value) -> {
            try {
                switch (Partner.valueOf(change.toUpperCase())) {
                    case NAME:
                        partnerMeta.setName((String) value);
                        break;
                    case TYPE:
                        partnerMeta.setType((String) value);
                        break;
                    case HASVEHICLE:
                        partnerMeta.setHasVehicle((Boolean) value);
                        break;
                    case HASSTATION:
                        partnerMeta.setHasStation((Boolean) value);
                        break;
                    case HASSTATIONSTATUS:
                        partnerMeta.setHasStationStatus((Boolean) value);
                        break;
                    case HASNOPARKINGZONE:
                        partnerMeta.setHasNoParkingZone((Boolean) value);
                        break;
                    case HASPREFPARKINGZONE:
                        partnerMeta.setHasPrefParkingZone((Boolean) value);
                        break;
                    case HASSPEEDLIMITZONE:
                        partnerMeta.setHasSpeedLimitZone((Boolean) value);
                        break;
                    case HASOPERATINGZONE:
                        partnerMeta.setHasOperatingZone((Boolean) value);
                        break;
                    case HASPARKING:
                        partnerMeta.setHasParking((Boolean) value);
                        break;
                    case HASHOLD:
                        partnerMeta.setHasHold((Boolean) value);
                        break;
                    case URLWEBVIEW:
                        partnerMeta.setUrlWebview((Boolean) value);
                        break;
                    case OPERATOR:
                        partnerMeta.setOperator((String) value);
                        break;
                    case URL:
                        partnerMeta.setUrl((String) value);
                        break;
                    case LOGOURL:
                        partnerMeta.setLogoUrl((String) value);
                        break;
                    case LOGOFORMAT:
                        partnerMeta.setLogoFormat((String) value);
                        break;
                    case PRIMARYCOLOR:
                        partnerMeta.setPrimaryColor((String) value);
                        break;
                    case SECONDARYCOLOR:
                        partnerMeta.setSecondaryColor((String) value);
                        break;
                    case ISENABLED:
                        partnerMeta.setIsEnabled((Boolean) value);
                        break;
                    case PRICELIST:
                        updatePriceList(updates, partnerMeta);
                        break;
                    default:
                        break;
                }
                // in case one field is used for patch but not authorized, create an exception
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Invalid field %s", change), e);
            }
        });

        // return the partnerMeta read from database
        partnerMetaRepository.save(partnerMeta);
        // be careful to use custom find operation to get partner and not automatic one
        // because of specific behavior on duration & distance
        return findPartnerMetaById(partnerId);
    }

    /**
     * Method to manage updatePrice exception outside main function.
     *
     * @param updates     The map representing the json to set
     * @param partnerMeta The partnerMeta which need to be enriched with the json
     */
    private void updatePriceList(Map<String, Object> updates, PartnerMeta partnerMeta) {
        try {
            partnerMeta.setPriceList(priceListService.updatePriceList(updates, partnerMeta));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid entries for priceList element", e);
        }
    }


}
