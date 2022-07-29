package com.gateway.database.repository;



import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.Headers;



@Repository
public interface HeadersRepository extends CrudRepository<Headers, UUID>{


}
