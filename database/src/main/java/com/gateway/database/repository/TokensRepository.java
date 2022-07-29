package com.gateway.database.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.Token;

@Repository
public interface TokensRepository extends CrudRepository<Token, UUID> {
	public Token findByMspMspId(UUID mspMetaId);
}
