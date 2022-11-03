package com.gateway.database.repository;

import com.gateway.database.model.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokensRepository extends CrudRepository<Token, UUID> {
	 Token findByPartnerPartnerId(UUID partnerMetaId);
}
