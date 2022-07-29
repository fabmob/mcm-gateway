package com.gateway.database.repository;

import com.gateway.database.model.MspStandard;
import com.gateway.database.model.StandardPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MspStandardRepository extends CrudRepository<MspStandard, StandardPK> {

    @Query(value = "SELECT standard FROM MspStandard standard " +
            "WHERE ((:mspMetaId) IS NULL OR CAST(standard.id.msp.mspId as org.hibernate.type.UUIDCharType) IN (:mspMetaId))" +
            "   AND ((:mspActionsId) IS NULL OR CAST(standard.id.action.mspActionId as org.hibernate.type.UUIDCharType) IN (:mspActionsId))"+
            "	AND ((:mspActionsName  IS NULL OR  standard.id.action.action = :mspActionsName))" +
            "	AND ((:versionStandard IS NULL OR standard.id.versionStandard = :versionStandard))" +
            "	AND ((:versionDatamapping IS NULL OR standard.id.versionDataMapping = :versionDatamapping))"+
            "	AND ((:isActive IS NULL OR standard.isActive = :isActive))"
    )
    public List<MspStandard> findByKeyPrimary(@Param("mspMetaId") UUID mspMetaId,
                                              @Param("mspActionsId") UUID mspActionsId,
                                              @Param("mspActionsName") String mspActionsName,
                                              @Param("versionStandard") String versionStandard,
                                              @Param("versionDatamapping") String versionDatamapping,
                                              @Param("isActive") Boolean isActive );

    public MspStandard findByMspStandardId(UUID mspStandardId);

    @Query("select standard from MspStandard standard where standard.adapter.id = ?1")
    public List<MspStandard> findByAdapterId(UUID AdapterId);

}



