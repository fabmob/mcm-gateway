package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.AdaptersMapper;
import com.gateway.dataapi.service.AdaptersService;
import com.gateway.database.model.Adapters;
import com.gateway.database.service.AdaptersDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdaptersServiceImp implements AdaptersService {
    @Autowired
    AdaptersDatabaseService adaptersDatabaseService;

    private final AdaptersMapper mapper = Mappers.getMapper(AdaptersMapper.class);

    /**
     * Add a new AdaptersDTO
     *
     * @param adaptersDTO AdaptersDTO object
     * @return AdaptersDTO informations for the AdaptersDTO added
     */
    @Override
    public AdaptersDTO addAdapters(AdaptersDTO adaptersDTO) {
        return mapper.mapEntityToDto(adaptersDatabaseService.addAdapter(mapper.mapDtoToEntity(adaptersDTO)));
    }

    /**
     * Retrieve a list of Adapters transported into AdaptersDTO
     *
     * @return List of AdaptersDTO
     */
    @Override
    public List<AdaptersDTO> getAllAdapters() {
        List<Adapters> adapters = adaptersDatabaseService.getAllAdapters();
        return mapper.mapEntityToDto(adapters);
    }

    /**
     * Delete a AdaptersDTO
     *
     * @param id Identifier of the AdaptersDTO
     */
    @Override
    public void deleteAdapters(UUID id) {
        adaptersDatabaseService.deleteAdapter(id);

    }

    /**
     * Retrieve a AdaptersDTO informations.
     *
     * @param id Identifier of the AdaptersDTO
     * @return AdaptersDTO informations for the AdaptersDTO
     * @throws NotFoundException not found object
     */
    @Override
    public AdaptersDTO getAdaptersFromId(UUID id) throws NotFoundException {
        Adapters adapterFound = adaptersDatabaseService.findAdapterById(id);
        return mapper.mapEntityToDto(adapterFound);
    }
}
