package com.gateway.database.repository;

import com.gateway.database.model.PartnerActions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerActionsRepository extends CrudRepository<PartnerActions, UUID> {


    @Query(value = "select  distinct act.* from msp.partner_actions  as act " +
            " inner join msp.partner_standard as stand  " +
            " on act.partner_action_id = stand.partner_action_id" +
            " WHERE stand.partner_id = :partnerMetaId", nativeQuery = true)
    List<PartnerActions> fetchByIdPartnerId(@Param("partnerMetaId") UUID partnerMetaId);
}
