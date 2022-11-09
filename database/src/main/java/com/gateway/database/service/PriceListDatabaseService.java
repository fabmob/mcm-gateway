package com.gateway.database.service;

import com.gateway.database.model.PartnerMeta;
import com.gateway.database.model.PriceList;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PriceListDatabaseService {

    /**
     * Retrieve a list of priceList.
     *
     * @return List of  priceList.
     */
    List<PriceList> findAllPriceList();

    /**
     * Retrieve a priceList information by id.
     *
     * @param id Identifier of the priceList
     * @return priceList  information for the priceList
     */
    PriceList findPriceListById(UUID id);

    /**
     * Create a new priceList
     *
     * @param priceList priceList object
     * @return priceList  information for the priceList posted
     */
    PriceList createPriceList(PriceList priceList);

    /**
     * Edit specific information of the priceList
     *
     * @param updates contains priceList object
     * @param mspMeta that contains the priceList object
     * @return priceList  information for the priceList patched
     */
    PriceList updatePriceList(Map<String, Object> updates, PartnerMeta mspMeta) throws IOException;

}
