package com.gateway.database.service;

import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PriceList;
import com.gateway.database.model.PriceListItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PriceListItemDatabaseService {

    /**
     * Retrieve a list of PriceListItem.
     *
     * @return List of  PriceListItem.
     */
    List<PriceListItem> findAllPriceListItem();

    /**
     * Retrieve a priceListItem information by id.
     *
     * @param id Identifier of the priceListItem
     * @return priceListItem  information for the priceListItem
     */
    PriceListItem findPriceListItemById(UUID id);

    /**
     * Create a new priceListItem
     *
     * @param priceListItem priceListItem object
     * @return priceList  information for the priceListItem posted
     */
    PriceListItem createPriceListItem(PriceListItem priceListItem);

    /**
     * Edit specific information of the Distance
     *
     * @param updates   contains Distance object
     * @param priceList that contains the Distance object
     * @return list of distances  information for the Distance patched
     * @throws IOException issue parsing json
     */
    List<Distance> updateDistance(Map<String, Object> updates, PriceList priceList) throws IOException;

    /**
     * Edit specific information of the Duration
     *
     * @param updates   contains Duration object
     * @param priceList that contains the Duration object
     * @return list of durations  information for the Duration patched
     * @throws IOException issue parsing json
     */
    List<Duration> updateDuration(Map<String, Object> updates, PriceList priceList) throws IOException;
}
