package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.*;
import com.gateway.database.repository.MspMetaRepository;
import com.gateway.database.repository.PriceListItemRepository;
import com.gateway.database.repository.PriceListRepository;
import com.gateway.database.service.MspMetaDatabaseService;
import com.gateway.database.service.PriceListDatabaseService;
import com.gateway.database.util.enums.Msp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gateway.commonapi.utils.CommonUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static com.gateway.database.util.constant.DataMessageDict.FIRST_PLACEHOLDER;
import static com.gateway.database.util.constant.DataMessageDict.MSP_META_WITH_ID_IS_NOT_FOUND;

@Slf4j
@Service
public class MspMetaDatabaseServiceImpl implements MspMetaDatabaseService {

    @Autowired
    private MspMetaRepository mspMetaRepository;

    @Autowired
    private PriceListItemRepository priceListItemRepository;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListDatabaseService priceListService;

    @Autowired
    private ErrorMessages errorMessage;

    @Override
    public List<MspMeta> findAllMspMeta() {
        return (List<MspMeta>) mspMetaRepository.findAll();
    }

    @Override
    public MspMeta findMspMetaById(UUID mspId) {

        MspMeta msp = mspMetaRepository.findById(mspId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_META_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, mspId.toString()))));

        if (null != msp.getPriceList()) {
            // retrieve both list of distance and duration
            List<Distance> distanceList = priceListItemRepository
                    .findAllDistancePriceListId(msp.getPriceList().getPriceListId());
            List<Duration> durationList = priceListItemRepository
                    .findAllDurationPriceListId(msp.getPriceList().getPriceListId());

            // attach them to the pricelists corresponding
            msp.getPriceList().setDuration(durationList);
            msp.getPriceList().setDistance(distanceList);
            if (log.isDebugEnabled()) {
                log.debug("Msp ID {}", msp.getMspId());
                if (msp.getPriceList() != null) {
                    log.debug("\tPricelist ID {}", msp.getPriceList().getPriceListId());
                    log.debug("\tPricelist duration size {}", msp.getPriceList().getDuration().size());
                    log.debug("\tPricelist distance size {}", msp.getPriceList().getDistance().size());
                }
            }
        }
        return msp;

    }

    @Override
    public MspMeta createMspMeta(MspMeta mspMeta) {
        if (null != mspMeta.getPriceList()) {

            // we save the priceList Without items as first we need element to be stored in
            // order to have the primarykey
            PriceList createdPriceList = priceListService.createPriceList(mspMeta.getPriceList());
            // now we save the mspMeta object
            mspMeta.setPriceList(createdPriceList);
        }
        return mspMetaRepository.save(mspMeta);
    }

    @Override
    public void removeMspMeta(UUID id) {
        MspMeta msp = findMspMetaById(id);
        PriceList priceListToRemove = msp.getPriceList();
        mspMetaRepository.deleteById(id);
        if (null != priceListToRemove) {
            msp.getPriceList().getDuration()
                    .forEach(duration -> priceListItemRepository.removePricelistId(duration.getPriceListItemId()));
            msp.getPriceList().getDistance()
                    .forEach(distance -> priceListItemRepository.removePricelistId(distance.getPriceListItemId()));
            msp.getPriceList().setMspMeta(null);
            // remove the mspMeta
            priceListRepository.removePricelistById(priceListToRemove.getPriceListId());
        }
    }

    /**
     * Update full mspMeta entity
     *
     * @param id      Identifier of the mspMeta
     * @param mspMeta MspMeta object that represents the entity to update
     */
    @Override
    public void updateMspMeta(UUID id, MspMeta mspMeta) {
        mspMeta.setMspId(id);
        if (null != mspMeta.getPriceList()) {

            if (findMspMetaById(id).getPriceList() == null) {
                // creation of the pricelist that does not exist yet in database
                mspMeta.setPriceList(priceListService.createPriceList(mspMeta.getPriceList()));
                mspMetaRepository.save(mspMeta);
            }
            PriceList priceListUpdate = mspMeta.getPriceList();
            priceListUpdate.setPriceListId(findMspMetaById(id).getPriceList().getPriceListId());

            List<Duration> durationUpdate = priceListUpdate.getDuration();
            List<Distance> distanceUpdate = priceListUpdate.getDistance();
            try {
                // if some element has uuid on pricelistItem, get it. For other elements remove
                // and create
                List<Duration> duration = findMspMetaById(id).getPriceList().getDuration();
                List<Distance> distance = findMspMetaById(id).getPriceList().getDistance();

                // manage duration
                updateDurationItems(id, durationUpdate, duration);

                // manage Distance
                updateDistanceItems(id, distanceUpdate, distance);

                mspMeta.setPriceList(priceListUpdate);
                mspMetaRepository.save(mspMeta);
            } catch (Exception e) {
                log.error("Error updating object mspMeta with id : {}", id, e);
            }
        }
        mspMetaRepository.save(mspMeta);
    }

    /**
     * Compare the list of duration from dto to update with the list of duration in
     * database
     *
     * @param mspId          id of the msp
     * @param durationUpdate list of duration from dto
     * @param duration       list of duration from database
     */
    @Override
    public void updateDurationItems(UUID mspId, List<Duration> durationUpdate, List<Duration> duration) {
        Set<UUID> durationsFromDatabase = new HashSet<>();
        PriceList pricelistElement = findMspMetaById(mspId).getPriceList();

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
     * @param mspId          id of the msp
     * @param distanceUpdate list of distance from dto
     * @param distance       list of distance from database
     */
    @Override
    public void updateDistanceItems(UUID mspId, List<Distance> distanceUpdate, List<Distance> distance) {
        Set<UUID> distancesFromDatabase = new HashSet<>();
        PriceList pricelistElement = findMspMetaById(mspId).getPriceList();

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
    public MspMeta updateMspMeta(Map<String, Object> updates, UUID mspId) {
        MspMeta mspMeta = this.findMspMetaById(mspId);

        updates.forEach((change, value) -> {
            try {
                switch (Msp.valueOf(change.toUpperCase())) {
                    case NAME:
                        mspMeta.setName((String) value);
                        break;
                    case TYPE:
                        mspMeta.setType((String) value);
                        break;
                    case HASVEHICLE:
                        mspMeta.setHasVehicle((Boolean) value);
                        break;
                    case HASSTATION:
                        mspMeta.setHasStation((Boolean) value);
                        break;
                    case HASSTATIONSTATUS:
                        mspMeta.setHasStationStatus((Boolean) value);
                        break;
                    case HASNOPARKINGZONE:
                        mspMeta.setHasNoParkingZone((Boolean) value);
                        break;
                    case HASPREFPARKINGZONE:
                        mspMeta.setHasPrefParkingZone((Boolean) value);
                        break;
                    case HASSPEEDLIMITZONE:
                        mspMeta.setHasSpeedLimitZone((Boolean) value);
                        break;
                    case HASOPERATINGZONE:
                        mspMeta.setHasOperatingZone((Boolean) value);
                        break;
                    case HASPARKING:
                        mspMeta.setHasParking((Boolean) value);
                        break;
                    case HASHOLD:
                        mspMeta.setHasHold((Boolean) value);
                        break;
                    case URLWEBVIEW:
                        mspMeta.setUrlWebview((Boolean) value);
                        break;
                    case OPERATOR:
                        mspMeta.setOperator((String) value);
                        break;
                    case URL:
                        mspMeta.setUrl((String) value);
                        break;
                    case LOGOURL:
                        mspMeta.setLogoUrl((String) value);
                        break;
                    case LOGOFORMAT:
                        mspMeta.setLogoFormat((String) value);
                        break;
                    case PRIMARYCOLOR:
                        mspMeta.setPrimaryColor((String) value);
                        break;
                    case SECONDARYCOLOR:
                        mspMeta.setSecondaryColor((String) value);
                        break;
                    case ISENABLED:
                        mspMeta.setIsEnabled((Boolean) value);
                        break;
                    case PRICELIST:
                        updatePriceList(updates, mspMeta);
                        break;
                }
                // in case one field is used for patch but not authorized, create an exception
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Invalid field %s", change), e);
            }
        });

        // return the mspMeta read from database
        mspMetaRepository.save(mspMeta);
        // be carefull to use custom find operation to get msp and not automatic one
        // because of specific behavior on duration & distance
        return findMspMetaById(mspId);
    }

    /**
     * Method to manage updatePrice exception outsime main function.
     *
     * @param updates The map representing the json to set
     * @param mspMeta The mspMeta wich need to be enriched with the json
     */
    private void updatePriceList(Map<String, Object> updates, MspMeta mspMeta) {
        try {
            mspMeta.setPriceList(priceListService.updatePriceList(updates, mspMeta));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid entries for priceList element", e);
        }
    }


}
