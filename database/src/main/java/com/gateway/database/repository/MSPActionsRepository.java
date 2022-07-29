package com.gateway.database.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.MSPActions;

@Repository
public interface MSPActionsRepository extends CrudRepository<MSPActions, UUID> {


    @Query(value = "select  distinct act.* from msp.msp_actions  as act " +
            " inner join msp.msp_standard as stand  " +
            " on act.msp_action_id = stand.msp_action_id" +
            " WHERE stand.msp_id = :mspMetaId", nativeQuery = true)
    public List<MSPActions> fetchByIdMspId(@Param("mspMetaId") UUID mspMetaId);
}
