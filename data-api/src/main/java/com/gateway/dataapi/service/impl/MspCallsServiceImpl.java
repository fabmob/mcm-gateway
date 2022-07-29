package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.MspCallsDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.MspCallsMapper;
import com.gateway.dataapi.service.MspCallsService;
import com.gateway.database.model.MSPCalls;
import com.gateway.database.service.MSPCallsDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MspCallsServiceImpl implements MspCallsService {
    @Autowired
    MSPCallsDatabaseService callService;
    private final MspCallsMapper mapper = Mappers.getMapper(MspCallsMapper.class);

    /**
     * Add a new MspCallsDto
     *
     * @param callDTO MspCallsDTO object
     * @return MspCallsDTO informations for the MspCallsDTO added
     */
    @Override
    public MspCallsDTO addMspCalls(MspCallsDTO callDTO) {
        return mapper.mapEntityToDto(callService.addMspCall(mapper.mapDtoToEntity(callDTO)));
    }

    /**
     * Retrieve a list of MspCalls transported into MspCallsDto
     *
     * @return List of MspCallsDTO
     */
    @Override
    public List<MspCallsDTO> getAllMspCalls() {
        List<MSPCalls> calls = callService.getAllCalls();
        return mapper.mapEntityToDto(calls);
    }

    /**
     * Update all the MspCalls informations
     *
     * @param id          Identifier of the MspCallsDTO
     * @param callDTO MspCallsDTO object
     * @return MspCallsDTO informations for the MspCallsDTO puted
     */
    @Override
    public MspCallsDTO updateMspCalls(UUID id, MspCallsDTO callDTO) {
        MSPCalls callupdate = callService.updateMspCall(id, mapper.mapDtoToEntity(callDTO));
        return mapper.mapEntityToDto(callupdate);
    }

    /**
     * Delete a MspCallsDTO
     *
     * @param id Identifier of the MspCallsDTO
     */
    @Override
    public void deleteMspCalls(UUID id) {
        callService.deleteMspCall(id);
    }

    /**
     * Retrieve a MspCallsDto informations.
     *
     * @param id Identifier of the MspCallsDto
     * @return MspCallsDto informations for the MspCallsDto
     * @throws NotFoundException not found object
     */
    @Override
    public MspCallsDTO getMspCallsFromId(UUID id) throws NotFoundException {
        MSPCalls mspCall = callService.findMspCallsById(id);
        return mapper.mapEntityToDto(mspCall);
    }

    /**
     * Get MspCallsDto from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return MspCallsDto
     * @throws NotFoundException
     */

    @Override
    public List<MspCallsDTO> getByActionId(UUID id) {
        List<MSPCalls> mpscalls = callService.findByActionMspActionId(id);
        return mapper.mapEntityToDto(mpscalls);
    }

}
