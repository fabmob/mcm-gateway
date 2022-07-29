package com.gateway.database.service;

import com.gateway.database.model.Distance;
import com.gateway.database.model.Duration;
import com.gateway.database.model.MspMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MspMetaDatabaseService {

    /**
     * Retrieve a list of mspMeta.
     *
     * @return List of  MspMeta.
     */
    List<MspMeta> findAllMspMeta();

    /**
     * Retrieve a MspMeta informations.
     *
     * @param id Identifier of the mspMeta
     * @return MspMeta  informations for the mspMeta
     */
    MspMeta findMspMetaById(UUID id);

    /**
     * Create a new MspMeta
     *
     * @param mspMeta MspMeta object
     * @return MspMeta  informations for the MspMeta posted
     */
    MspMeta createMspMeta(MspMeta mspMeta);

    /**
     * Remove a mspMeta
     *
     * @param id Identifier of the mspMeta
     */
    void removeMspMeta(UUID id);

    /**
     * Update all the MspMeta informations
     *
     * @param id      Identifier of the mspMeta
     * @param mspMeta MspMeta object
     */
    void updateMspMeta(UUID id, MspMeta mspMeta);

    /**
     * Edit specific information of the MspMeta
     *
     * @param updates contains MspMeta object
     * @param id      Identifier of the mspMeta
     * @return MspMeta  informations for the MspMeta patched
     */
    MspMeta updateMspMeta(Map<String, Object> updates, UUID id);

    void updateDurationItems(UUID mspId, List<Duration> durationUpdate, List<Duration> duration);

    void updateDistanceItems(UUID mspId, List<Distance> distanceUpdate, List<Distance> distance);

}