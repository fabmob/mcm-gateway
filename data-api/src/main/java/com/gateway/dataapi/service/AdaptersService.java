package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface AdaptersService {
    /**
     * Add a new AdaptersDTO
     *
     * @param adaptersDTO AdaptersDTO object
     * @return AdaptersDTO informations for the AdaptersDTO added
     */
    public AdaptersDTO addAdapters(AdaptersDTO adaptersDTO);

    /**
     * Retrieve a list of Adapters transported into AdaptersDTO
     *
     * @return List of AdaptersDTO
     */
    public List<AdaptersDTO> getAllAdapters();

    /**
     * Delete a AdaptersDTO
     *
     * @param id Identifier of the AdaptersDTO
     */
    public void deleteAdapters(UUID id);

    /**
     * Retrieve a AdaptersDTO informations.
     *
     * @param id Identifier of the AdaptersDTO
     * @return AdaptersDTO informations for the AdaptersDTO
     * @throws NotFoundException not found object
     */
    public AdaptersDTO getAdaptersFromId(UUID id) throws NotFoundException;
}
