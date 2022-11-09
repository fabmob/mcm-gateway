package com.gateway.database.repository;

import com.gateway.database.model.GatewayParams;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GatewayParamsRepository extends CrudRepository<GatewayParams, String> {
}
