package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * data Mapping interface between the PartnerMeta object in the database and the one exposed by the web service
 */
public interface PartnerMetaService {
    /**
     * Retrieve a list of PartnerMeta transported into PartnerMetaDTO
     *
     * @return List of  PartnerMetaDTO
     */
    List<PartnerMetaDTO> getPartnerMetasByPartnerType(PartnerTypeEnum partnerType);

    /**
     * Retrieve a list of PartnerMeta transported into PartnerMetaDto
     *
     * @return List of  PartnerMetaDTO
     */
    List<PartnerMetaDTO> getPartnerMetas();

    /**
     * Retrieve a PartnerMetaDto information.
     *
     * @param id Identifier of the PartnerMetaDto
     * @return PartnerMetaDto  information for the PartnerMetaDto
     * @throws NotFoundException not found object
     */
    PartnerMetaDTO getPartnerMeta(UUID id) throws NotFoundException;

    /**
     * Post a new PartnerMetaDto
     *
     * @param partnerMetaDTO PartnerMetaDTO object
     * @return PartnerMetaDTO  information for the PartnerMetaDTO posted
     */
    PartnerMetaDTO postPartnerMeta(PartnerMetaDTO partnerMetaDTO);


    /**
     * Update all the PartnerMeta information
     *
     * @param id             Identifier of the PartnerMetaDTO
     * @param partnerMetaDTO PartnerMetaDTO object
     * @return PartnerMetaDTO  information for the PartnerMetaDTO put
     */
    void putPartnerMeta(UUID id, PartnerMetaDTO partnerMetaDTO);

    /**
     * Delete a PartnerMetaDTO
     *
     * @param id Identifier of the PartnerMetaDTO
     */
    void deletePartnerMeta(UUID id);

    /**
     * Update edit specific information of the PartnerMetaDTO
     *
     * @param updates contains PartnerMetaDTO object
     * @param id      Identifier of the PartnerMetaDTO
     * @return PartnerMetaDTO  information for the PartnerMetaDTO patched
     */
    PartnerMetaDTO patchPartnerMeta(Map<String, Object> updates, UUID id);

    /**
     * Refresh partners' meta-data in cache
     */
    void refreshCachePartnerMetas();
}
