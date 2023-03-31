package com.gateway.database.repository;

import com.gateway.database.model.PartnerMeta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerMetaRepository extends CrudRepository<PartnerMeta, UUID>, QueryByExampleExecutor<PartnerMeta> {
    List<PartnerMeta> findByPartnerType(String partnerType);

    List<PartnerMeta> findByType(String type);

    @Query(value = "SELECT partnerMeta FROM PartnerMeta partnerMeta WHERE UPPER(partnerMeta.type) LIKE UPPER(CONCAT('%', :type, '%')) AND (:partnerType IS NULL OR partnerMeta.partnerType = :partnerType)")
    List<PartnerMeta> findByType(@Param("type") String type, @Param("partnerType") String partnerType);

    @Query(value = "SELECT partnerMeta FROM PartnerMeta partnerMeta WHERE UPPER(partnerMeta.name) LIKE UPPER(CONCAT('%', :name, '%')) AND (:partnerType IS NULL OR partnerMeta.partnerType = :partnerType)")
    List<PartnerMeta> findByNameContainingIgnoreCase(@Param("name") String name, @Param("partnerType") String partnerType);

    @Query(value = "SELECT partnerMeta FROM PartnerMeta partnerMeta WHERE UPPER(partnerMeta.operator) LIKE UPPER(CONCAT('%', :operator, '%')) AND (:partnerType IS NULL OR partnerMeta.partnerType = :partnerType)")
    List<PartnerMeta> findByOperatorContainingIgnoreCase(@Param("operator") String operator, @Param("partnerType") String partnerType);
}
