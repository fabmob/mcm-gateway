package com.gateway.database.repository;

import com.gateway.database.model.Adapters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AdaptersRepository extends CrudRepository<Adapters, UUID> {

}


