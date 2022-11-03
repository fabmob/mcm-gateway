package com.gateway.database.repository;

import com.gateway.database.model.Body;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BodyRepository extends CrudRepository<Body, UUID>{
	

}
