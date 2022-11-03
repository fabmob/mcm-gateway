package com.gateway.database.service;

import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.PartnerMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PartnerMetaDatabaseService {

    /**
     * Retrieve a list of partnerMeta.
     *
     * @return List of  partnerMeta.
     */
    List<PartnerMeta> findAllByPartnerType(String partnerType);

    /**
     * Retrieve a list of PartnerMeta.
     *
     * @return List of  PartnerMeta.
     */
    List<PartnerMeta> findAllPartnerMeta();

    /**
     * Retrieve a PartnerMeta information.
     *
     * @param id Identifier of the PartnerMeta
     * @return PartnerMeta  information for the PartnerMeta
     */
    PartnerMeta findPartnerMetaById(UUID id);

    /**
     * Create a new PartnerMeta
     *
     * @param partnerMeta PartnerMeta object
     * @return PartnerMeta  information for the PartnerMeta posted
     */
    PartnerMeta createPartnerMeta(PartnerMeta partnerMeta);

    /**
     * Remove a PartnerMeta
     *
     * @param id Identifier of the PartnerMeta
     */
    void removePartnerMeta(UUID id);

    /**
     * Update all the PartnerMeta information
     *
     * @param id          Identifier of the PartnerMeta
     * @param partnerMeta PartnerMeta object
     */
    void updatePartnerMeta(UUID id, PartnerMeta partnerMeta);

    /**
     * Edit specific information of the PartnerMeta
     *
     * @param updates contains PartnerMeta object
     * @param id      Identifier of the PartnerMeta
     * @return PartnerMeta  information for the PartnerMeta patched
     */
    PartnerMeta updatePartnerMeta(Map<String, Object> updates, UUID id);

    void updateDurationItems(UUID mspId, List<Duration> durationUpdate, List<Duration> duration);

    void updateDistanceItems(UUID mspId, List<Distance> distanceUpdate, List<Distance> distance);

}