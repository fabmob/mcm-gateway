package com.gateway.database.repository;

import com.gateway.database.model.PriceList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface PriceListRepository extends CrudRepository<PriceList, UUID> {
    /**
     * Remove priceList with provided priceListId
     *
     * @param priceListId the uuid of priceListItem to remove
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM #{#entityName} priceListItem"
            + " WHERE priceListItem.priceListId = :priceListId")
    void removePricelistById(@Param("priceListId") UUID priceListId);
}
