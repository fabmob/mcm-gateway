package com.gateway.database.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.HeadersValuesTemplate;

@Repository
public interface HeadersValuesTemplateRepository extends CrudRepository<HeadersValuesTemplate, UUID> {

}
