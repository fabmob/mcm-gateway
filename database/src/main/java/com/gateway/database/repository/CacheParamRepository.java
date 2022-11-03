package com.gateway.database.repository;

import com.gateway.database.model.CacheParam;
import com.gateway.database.model.CacheParamPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CacheParamRepository extends CrudRepository<CacheParam, CacheParamPK> {
    CacheParam findByCacheParamId(UUID cacheParamId);

    @Query(value = "SELECT param FROM CacheParam param " +
            "WHERE ((:actionType IS NULL OR param.cacheParamPK.actionType = :actionType))" +
            "   AND ((:partnerId) IS NULL OR CAST(param.cacheParamPK.partner.partnerId as org.hibernate.type.UUIDCharType) IN (:partnerId))"
    )
    List<CacheParam> findByCriteria(@Param("actionType") String actionType,
                                           @Param("partnerId") UUID partnerId);
}
