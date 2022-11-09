package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.PartnerActionMapper;
import com.gateway.dataapi.service.PartnerActionsService;
import com.gateway.database.model.PartnerActions;
import com.gateway.database.service.PartnerActionsDatabaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartnerActionsServiceImp implements PartnerActionsService {

    @Autowired
    private PartnerActionsDatabaseService actionService;

    private final PartnerActionMapper mapper = Mappers.getMapper(PartnerActionMapper.class);

    /**
     * Add a new PartnerActionDto
     *
     * @param actionDto PartnerActionDTO object
     * @return PartnerActionDTO information for the PartnerActionDTO added
     */
    @Override
    public PartnerActionDTO addPartnerAction(PartnerActionDTO actionDto) {
        PartnerActions action = actionService.addPartnerAction(mapper.mapDtoToEntity(actionDto));
        return mapper.mapEntityToDto(action);
    }

    /**
     * Retrieve a list of PartnerActions transported into PartnerActionsDto
     *
     * @return List of PartnerActionsDTO
     */
    @Override
    public List<PartnerActionDTO> getAllPartnerActions() {
        List<PartnerActions> actions = actionService.getAllPartnerActions();
        return mapper.mapEntityToDto(actions);
    }

    /**
     * Retrieve a list of PartnerActions transported into PartnerActionsDto
     *
     * @return List of PartnerActionsDTO
     */

    @Override
    public PartnerActionDTO updatePartnerAction(UUID id, PartnerActionDTO actionDto) {
        PartnerActions saveAction = actionService.updatePartnerAction(id, mapper.mapDtoToEntity(actionDto));
        return mapper.mapEntityToDto(saveAction);
    }

    /**
     * Delete a PartnerActionsDTO
     *
     * @param id Identifier of the PartnerActionsDTO
     */
    @Override
    public void deletePartnerAction(UUID id) {
        actionService.deletePartnerAction(id);
    }

    /**
     * Retrieve a PartnerActionDto information.
     *
     * @param id Identifier of the PartnerActionDto
     * @return PartnerActionDto information for the PartnerActionDto
     * @throws NotFoundException not found object
     */
    @Override
    public PartnerActionDTO getPartnerActionFromId(UUID id) throws NotFoundException {
        PartnerActions partnerAction = actionService.findPartnerActionById(id);
        return mapper.mapEntityToDto(partnerAction);
    }

    /**
     * Retrieve a PartnerActionDto information.
     *
     * @param id Identifier of the PartnerMeta
     * @return PartnerActionDto information for the PartnerActionDto
     * @throws NotFoundException not found object
     */
    @Override
    public List<PartnerActionDTO> getPartnerActionFromIdPartnerMetaId(UUID id) throws NotFoundException {
        List<PartnerActions> partnerActions = actionService.findByPartnerMetaId(id);
        return mapper.mapEntityToDto(partnerActions);
    }


}
