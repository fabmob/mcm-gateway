package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.MSPActions;
import com.gateway.database.repository.MSPActionsRepository;
import com.gateway.database.repository.SelectorRepository;
import com.gateway.database.service.MSPActionsDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;


@Service
public class MSPActionsDatabaseServiceImpl implements MSPActionsDatabaseService {

    @Autowired
    private MSPActionsRepository actionRepository;

    @Autowired
    private SelectorRepository selectorRepository;

    @Autowired
    private ErrorMessages errorMessages;

    public MSPActionsDatabaseServiceImpl(MSPActionsRepository actionRepository, SelectorRepository selectorRepository) {
        super();
        this.actionRepository = actionRepository;
        this.selectorRepository = selectorRepository;
    }

    public MSPActionsDatabaseServiceImpl() {
    }

    public MSPActionsRepository getActionRepository() {
        return actionRepository;
    }

    /**
     * Add a new MspActions
     *
     * @param action MspActions object
     * @return MspActions information for the MspActions added
     */
    @Override
    public MSPActions addMspAction(MSPActions action) {
        MSPActions mspAction = new MSPActions();
        if (null != action) {
            if (action.getSelector() != null && action.getSelector().getSelectorId() != null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(SELECTOR_WITH_ID_SHOULD_NOT_BE_PROVIDED, FIRST_PLACEHOLDER, String.valueOf(action.getSelector().getSelectorId()))));
            }
        }
        return actionRepository.save(action);
    }

    /**
     * Retrieve a list of MspActions transported into MspActions
     *
     * @return List of MspActions
     */

    @Override
    public List<MSPActions> getAllMspActions() {
        return (List<MSPActions>) actionRepository.findAll();

    }

    /**
     * Update all the MspActions information
     *
     * @param id     Identifier of the MspActions
     * @param action MspActions object
     * @return MspActions information for the MspActions put
     */

    @Override
    public MSPActions updateMspAction(UUID id, MSPActions action) {
        action.setMspActionId(id);
        try {
            actionRepository.save(action);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, action.getMspActionId().toString())));
        }
        return action;
    }

    /**
     * Delete a MspActions
     *
     * @param id Identifier of the MspActions
     */
    @Override
    public void deleteMspAction(UUID id) {
        try {
            actionRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
    }

    /**
     * Retrieve a MspActions information.
     *
     * @param id Identifier of the MspActions
     * @return MspActions information for the MspActions
     */
    @Override
    public MSPActions findMspActionById(UUID id) {
        MSPActions mspAction = actionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));

        return mspAction;
    }

    /**
     * Retrieve a MspActions informations.
     *
     * @param id Identifier of the MspMeta
     * @return MspActions informations for the MspActions
     */
    @Override
    public List<MSPActions> findByMspMetaId(UUID id) {
        List<MSPActions> actions = actionRepository.fetchByIdMspId(id);
        if (actions == null || actions.isEmpty()) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_MSP_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return actions;
    }
}


