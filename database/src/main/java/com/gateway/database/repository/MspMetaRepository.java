package com.gateway.database.repository;

import com.gateway.database.model.MspMeta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MspMetaRepository extends CrudRepository<MspMeta, UUID> {

}
