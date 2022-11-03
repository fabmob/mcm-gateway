package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.PartnerActions;
import com.gateway.database.repository.PartnerActionsRepository;
import com.gateway.database.repository.SelectorRepository;
import com.gateway.database.service.PartnerActionsDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;


@Service
public class PartnerActionsDatabaseServiceImpl implements PartnerActionsDatabaseService {

    @Autowired
    private PartnerActionsRepository actionRepository;

    @Autowired
    private SelectorRepository selectorRepository;

    @Autowired
    private ErrorMessages errorMessages;

    public PartnerActionsDatabaseServiceImpl(PartnerActionsRepository actionRepository, SelectorRepository selectorRepository) {
        super();
        this.actionRepository = actionRepository;
        this.selectorRepository = selectorRepository;
    }

    public PartnerActionsDatabaseServiceImpl() {
    }

    public PartnerActionsRepository getActionRepository() {
        return actionRepository;
    }

    /**
     * Add a new PartnerActions
     *
     * @param action PartnerActions object
     * @return PartnerActions information for the PartnerActions added
     */
    @Override
    public PartnerActions addPartnerAction(PartnerActions action) {
        if (null != action && action.getSelector() != null && action.getSelector().getSelectorId() != null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(SELECTOR_WITH_ID_SHOULD_NOT_BE_PROVIDED, FIRST_PLACEHOLDER, String.valueOf(action.getSelector().getSelectorId()))));
        }
        return actionRepository.save(action);
    }

    /**
     * Retrieve a list of PartnerActions transported into PartnerActions
     *
     * @return List of PartnerActions
     */

    @Override
    public List<PartnerActions> getAllPartnerActions() {
        return (List<PartnerActions>) actionRepository.findAll();

    }

    /**
     * Update all the PartnerActions information
     *
     * @param id     Identifier of the PartnerActions
     * @param action PartnerActions object
     * @return PartnerActions information for the PartnerActions put
     */

    @Override
    public PartnerActions updatePartnerAction(UUID id, PartnerActions action) {
        action.setPartnerActionId(id);
        PartnerActions partnerActionsInDB = this.findPartnerActionById(id);
        if (!Objects.equals(action.getAction(), partnerActionsInDB.getAction())) {
            throw new ConflictException(UPDATE_ACTION_NAME_IS_FORBIDDEN);
        }
        try {
            actionRepository.save(action);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, action.getPartnerActionId().toString())));
        }
        return action;
    }

    /**
     * Delete a PartnerActions
     *
     * @param id Identifier of the PartnerActions
     */
    @Override
    public void deletePartnerAction(UUID id) {
        try {
            actionRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
    }

    /**
     * Retrieve a PartnerActions information.
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerActions information for the PartnerActions
     */
    @Override
    public PartnerActions findPartnerActionById(UUID id) {
        return actionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));
    }

    /**
     * Retrieve a PartnerActions information.
     *
     * @param id Identifier of the PartnerMeta
     * @return PartnerActions information for the PartnerActions
     */
    @Override
    public List<PartnerActions> findByPartnerMetaId(UUID id) {
        List<PartnerActions> actions = actionRepository.fetchByIdPartnerId(id);
        if (actions == null || actions.isEmpty()) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_PARTNER_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return actions;
    }
}


