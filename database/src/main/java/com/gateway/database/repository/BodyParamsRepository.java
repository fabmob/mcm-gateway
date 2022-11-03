package com.gateway.database.repository;

import com.gateway.database.model.BodyParams;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface BodyParamsRepository extends CrudRepository<BodyParams, UUID>{

}
