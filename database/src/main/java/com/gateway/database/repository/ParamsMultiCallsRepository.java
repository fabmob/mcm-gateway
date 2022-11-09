package com.gateway.database.repository;

import com.gateway.database.model.ParamsMultiCalls;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ParamsMultiCallsRepository extends CrudRepository<ParamsMultiCalls, UUID>{

}
