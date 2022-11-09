package com.gateway.database.repository;

import com.gateway.database.model.PartnerStandard;
import com.gateway.database.model.StandardPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PartnerStandardRepository extends CrudRepository<PartnerStandard, StandardPK> {

    @Query(value = "SELECT standard FROM PartnerStandard standard " +
            "WHERE ((:partnerMetaId) IS NULL OR CAST(standard.id.partner.partnerId as org.hibernate.type.UUIDCharType) IN (:partnerMetaId))" +
            "AND ((:partnerActionsId) IS NULL OR CAST(standard.id.action.partnerActionId as org.hibernate.type.UUIDCharType) IN (:partnerActionsId))" +
            "AND ((:partnerActionsName  IS NULL OR  standard.id.action.action = :partnerActionsName))" +
            "AND ((:versionStandard IS NULL OR standard.id.versionStandard = :versionStandard))" +
            "AND ((:versionDatamapping IS NULL OR standard.id.versionDataMapping = :versionDatamapping))" +
            "AND ((:isActive IS NULL OR standard.isActive = :isActive))"
    )
    List<PartnerStandard> findByKeyPrimary(@Param("partnerMetaId") UUID partnerMetaId,
                                           @Param("partnerActionsId") UUID partnerActionsId,
                                           @Param("partnerActionsName") String partnerActionsName,
                                           @Param("versionStandard") String versionStandard,
                                           @Param("versionDatamapping") String versionDatamapping,
                                           @Param("isActive") Boolean isActive);

    PartnerStandard findByPartnerStandardId(UUID partnerStandardId);

    @Query("select standard from PartnerStandard standard where standard.adapter.id = ?1")
    List<PartnerStandard> findByAdapterId(UUID adapterId);

}



