package com.gateway.database.repository;

import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PriceListItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceListItemRepository extends CrudRepository<PriceListItem, UUID> {

    /**
     * Get the List of duration from price_list_item table
     * @param priceListId uuid of the pricelist
     * @return list of duration
     */
    @Query(value = "SELECT * from msp.price_list_item as item" +
            " WHERE item.dtype = 'duration' AND item.price_list_duration = :priceListId", nativeQuery = true)
    List<Duration> findAllDurationPriceListId(@Param("priceListId") UUID priceListId);

    /**
     * Get the List of distance from price_list_item table
     * @param priceListId uuid of the pricelist
     * @return list of distance
     */
    @Query(value = "SELECT * from msp.price_list_item as item" +
            " WHERE item.dtype = 'distance' AND item.price_list_distance = :priceListId", nativeQuery = true)
    List<Distance> findAllDistancePriceListId(@Param("priceListId") UUID priceListId);

    /**
     * Remove priceListItem with provided provided priceListItemId
     * @param priceListItemId the uuid of priceListItem to remove
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM #{#entityName} priceListItem"
            + " WHERE priceListItem.priceListItemId = :priceListItemId")
    void removePricelistId(@Param("priceListItemId") UUID priceListItemId);
}
