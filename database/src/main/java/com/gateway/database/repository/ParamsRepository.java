package com.gateway.database.repository;

import com.gateway.database.model.Params;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ParamsRepository extends CrudRepository<Params, UUID>{

}