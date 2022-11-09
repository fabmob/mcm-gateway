package com.gateway.database.repository;

import com.gateway.database.model.PartnerCalls;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerCallsRepository extends CrudRepository<PartnerCalls, UUID> {

	List<PartnerCalls> findByActionPartnerActionId(UUID partnerActionId);

    List<PartnerCalls> findAllByOrderByExecutionOrderAsc();
}
