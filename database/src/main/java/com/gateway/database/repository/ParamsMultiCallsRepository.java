package com.gateway.database.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.ParamsMultiCalls;


@Repository
public interface ParamsMultiCallsRepository extends CrudRepository<ParamsMultiCalls, UUID>{

}
