package com.gateway.database.repository;

import com.gateway.database.model.PartnerMeta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerMetaRepository extends CrudRepository<PartnerMeta, UUID> {
    List<PartnerMeta> findByPartnerType (String partnerType);
}
