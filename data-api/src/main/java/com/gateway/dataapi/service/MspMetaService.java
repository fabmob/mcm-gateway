package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * data Mapping interface between the mspMeta object in the database and the one exposed by the web service
 */
public interface MspMetaService {
    /**
     * Retrieve a list of MspMeta transported into MspMetaDto
     *
     * @return List of  MspMetaDTO
     */
    List<MspMetaDTO> getMspMetas();

    /**
     * Retrieve a MspMetaDto informations.
     *
     * @param id Identifier of the MspMetaDto
     * @return MspMetaDto  informations for the MspMetaDto
     * @throws NotFoundException not found object
     */
    MspMetaDTO getMspMeta(UUID id) throws NotFoundException;

    /**
     * Post a new MspMetaDto
     *
     * @param mspMetaDTO MspMetaDTO object
     * @return MspMetaDTO  informations for the MspMetaDTO posted
     */
    MspMetaDTO postMspMeta(MspMetaDTO mspMetaDTO);


    /**
     * Update all the MspMeta informations
     *
     * @param id         Identifier of the mspMetaDTO
     * @param mspMetaDTO mspMetaDTO object
     * @return MspMetaDTO  informations for the mspMetaDTO puted
     */
    void putMspMeta(UUID id, MspMetaDTO mspMetaDTO);

    /**
     * Delete a mspMetaDTO
     *
     * @param id Identifier of the mspMetaDTO
     */
    void deleteMspMeta(UUID id);

    /**
     * Update edit specific information of the mspMetaDTO
     *
     * @param updates contains mspMetaDTO object
     * @param id      Identifier of the mspMetaDTO
     * @return mspMetaDTO  informations for the mspMetaDTO patched
     */
    MspMetaDTO patchMspMeta(Map<String, Object> updates, UUID id);
}
