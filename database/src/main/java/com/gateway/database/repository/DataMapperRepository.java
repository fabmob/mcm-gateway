package com.gateway.database.repository;


import com.gateway.database.model.DataMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface DataMapperRepository extends CrudRepository<DataMapper, UUID> {

    List<DataMapper> findByActionPartnerActionId(UUID partnerActionId);

    Optional<List<DataMapper>> findByActionPartnerActionIdAndInternalField(UUID partnerActionId, String internalField);
}
