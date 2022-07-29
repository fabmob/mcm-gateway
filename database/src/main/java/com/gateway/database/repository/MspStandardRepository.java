package com.gateway.database.repository;

import com.gateway.database.model.MspStandard;
import com.gateway.database.model.StandardPK;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
            "	AND ((:mspActionsId) IS NULL OR CAST(standard.id.action.mspActionId as org.hibernate.type.UUIDCharType) IN (:mspActionsId))" +
            "	AND ((:versionStandard IS NULL OR standard.id.versionStandard = :versionStandard))" +
            "	AND ((:versionDatamapping IS NULL OR standard.id.versionDataMapping = :versionDatamapping))")
    public List<MspStandard> findByKeyPrimary(@Param("mspMetaId") UUID mspMetaId,
                                              @Param("mspActionsId") UUID mspActionsId,
                                              @Param("versionStandard") String versionStandard,
                                              @Param("versionDatamapping") String versionDatamapping);

    public MspStandard findByMspStandardId(UUID mspStandardId);


}



