package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.PartnerCallsDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.PartnerCallsMapper;
import com.gateway.dataapi.service.PartnerCallsService;
import com.gateway.database.model.PartnerCalls;
import com.gateway.database.service.PartnerCallsDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartnerCallsServiceImpl implements PartnerCallsService {
    @Autowired
    PartnerCallsDatabaseService callService;
    private final PartnerCallsMapper mapper = Mappers.getMapper(PartnerCallsMapper.class);

    /**
     * Add a new PartnerCallsDto
     *
     * @param callDTO PartnerCallsDTO object
     * @return PartnerCallsDTO information for the PartnerCallsDTO added
     */
    @Override
    public PartnerCallsDTO addPartnerCalls(PartnerCallsDTO callDTO) {
        return mapper.mapEntityToDto(callService.addPartnerCall(mapper.mapDtoToEntity(callDTO)));
    }

    /**
     * Retrieve a list of PartnerCalls transported into PartnerCallsDto
     *
     * @return List of PartnerCallsDTO
     */
    @Override
    public List<PartnerCallsDTO> getAllPartnerCalls() {
        List<PartnerCalls> calls = callService.getAllCalls();
        return mapper.mapEntityToDto(calls);
    }

    /**
     * Update all the PartnerCalls information
     *
     * @param id      Identifier of the PartnerCallsDTO
     * @param callDTO PartnerCallsDTO object
     * @return PartnerCallsDTO information for the PartnerCallsDTO put
     */
    @Override
    public PartnerCallsDTO updatePartnerCalls(UUID id, PartnerCallsDTO callDTO) {
        PartnerCalls callUpdate = callService.updatePartnerCall(id, mapper.mapDtoToEntity(callDTO));
        return mapper.mapEntityToDto(callUpdate);
    }

    /**
     * Delete a PartnerCallsDTO
     *
     * @param id Identifier of the PartnerCallsDTO
     */
    @Override
    public void deletePartnerCalls(UUID id) {
        callService.deletePartnerCall(id);
    }

    /**
     * Retrieve a PartnerCallsDto information.
     *
     * @param id Identifier of the PartnerCallsDto
     * @return PartnerCallsDto information for the PartnerCallsDto
     * @throws NotFoundException not found object
     */
    @Override
    public PartnerCallsDTO getPartnerCallsFromId(UUID id) throws NotFoundException {
        PartnerCalls partnerCall = callService.findPartnerCallsById(id);
        return mapper.mapEntityToDto(partnerCall);
    }

    /**
     * Get PartnerCallsDto from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerCallsDto
     * @throws NotFoundException
     */

    @Override
    public List<PartnerCallsDTO> getByActionId(UUID id) {
        List<PartnerCalls> partnerCalls = callService.findByActionPartnerActionId(id);
        return mapper.mapEntityToDto(partnerCalls);
    }

}
