package com.gateway.database.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.MSPCalls;

@Repository
public interface MSPCallsRepository extends CrudRepository<MSPCalls, UUID> {
	public List<MSPCalls> findByActionMspActionId(UUID actionId);

	public List<MSPCalls> findAllByOrderByExecutionOrderAsc();
}
