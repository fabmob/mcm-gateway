package com.gateway.dataapi.service.impl;

import java.util.List;
import java.util.UUID;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.MspActionMapper;
import com.gateway.dataapi.service.MspActionsService;
import com.gateway.database.model.MSPActions;
import com.gateway.database.service.MSPActionsDatabaseService;

@Service
public class MspActionsServiceImp implements MspActionsService {

    @Autowired
    private MSPActionsDatabaseService actionService;

    private final MspActionMapper mapper = Mappers.getMapper(MspActionMapper.class);

    /**
     * Add a new MspActionDto
     *
     * @param actionDto MspActionDTO object
     * @return MspActionDTO informations for the MspActionDTO added
     */
    @Override
    public MspActionDTO addMspAction(MspActionDTO actionDto) {
        MSPActions action = actionService.addMspAction(mapper.mapDtoToEntity(actionDto));
        return mapper.mapEntityToDto(action);
    }

    /**
     * Retrieve a list of MspActions transported into MspActionsDto
     *
     * @return List of MspActionsDTO
     */
    @Override
    public List<MspActionDTO> getAllMspActions() {
        List<MSPActions> actions = actionService.getAllMspActions();
        return mapper.mapEntityToDto(actions);
    }

    /**
     * Retrieve a list of MspActions transported into MspActionsDto
     *
     * @return List of MspActionsDTO
     */

    @Override
    public MspActionDTO updateMspAction(UUID id, MspActionDTO actionDto) {
        MSPActions saveaction = actionService.updateMspAction(id, mapper.mapDtoToEntity(actionDto));
        return mapper.mapEntityToDto(saveaction);
    }

    /**
     * Delete a MspActionsDTO
     *
     * @param id Identifier of the MspActionsDTO
     */
    @Override
    public void deleteMspAction(UUID id) {
        actionService.deleteMspAction(id);
    }

    /**
     * Retrieve a MspActionDto informations.
     *
     * @param id Identifier of the MspActionDto
     * @return MspActionDto informations for the MspActionDto
     * @throws NotFoundException not found object
     */
    @Override
    public MspActionDTO getMspActionFromId(UUID id) throws NotFoundException {
        MSPActions mspAction = actionService.findMspActionById(id);
        return mapper.mapEntityToDto(mspAction);
    }

    /**
     * Retrieve a MspActionDto informations.
     *
     * @param id Identifier of the MspMeta
     * @return MspActionDto informations for the MspActionDto
     * @throws NotFoundException not found object
     */
    @Override
    public List<MspActionDTO> getMspActionFromIdMspMetaId(UUID id) throws NotFoundException {
        List<MSPActions> mspActions = actionService.findByMspMetaId(id);
        return mapper.mapEntityToDto(mspActions);
    }


}
