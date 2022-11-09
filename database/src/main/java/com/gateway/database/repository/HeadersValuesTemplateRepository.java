package com.gateway.database.repository;

import com.gateway.database.model.HeadersValuesTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HeadersValuesTemplateRepository extends CrudRepository<HeadersValuesTemplate, UUID> {

}
