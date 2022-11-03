package com.gateway.database.repository;

import com.gateway.database.model.Selector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SelectorRepository extends CrudRepository<Selector, UUID>{

}
