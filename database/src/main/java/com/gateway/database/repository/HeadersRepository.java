package com.gateway.database.repository;


import com.gateway.database.model.Headers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface HeadersRepository extends CrudRepository<Headers, UUID>{


}
