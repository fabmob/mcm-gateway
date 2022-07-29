package com.gateway.database.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.Params;


@Repository
public interface ParamsRepository extends CrudRepository<Params, UUID>{

}