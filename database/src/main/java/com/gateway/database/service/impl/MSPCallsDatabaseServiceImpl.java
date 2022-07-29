package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.*;
import com.gateway.database.repository.BodyParamsRepository;
import com.gateway.database.repository.BodyRepository;
import com.gateway.database.repository.MSPCallsRepository;
import com.gateway.database.service.MSPCallsDatabaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

import static com.gateway.database.util.constant.DataMessageDict.*;


@Service
public class MSPCallsDatabaseServiceImpl implements MSPCallsDatabaseService {

    @Autowired
    private MSPCallsRepository callRepository;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private BodyParamsRepository bodyParamRepository;

    @Autowired
    private ErrorMessages errorMessages;


    public MSPCallsDatabaseServiceImpl(MSPCallsRepository callRepo, BodyRepository bodyRepo,
                                       BodyParamsRepository bodyParamRepo) {
        super();
        this.callRepository = callRepo;
        this.bodyRepository = bodyRepo;
        this.bodyParamRepository = bodyParamRepo;
    }

    public MSPCallsDatabaseServiceImpl() {
    }

    public BodyRepository getBodyRepository() {
        return bodyRepository;
    }

    /**
     * Validate Body to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call the call in which we check id UUID is present
     */
    private void validateUuidNotProvided(MSPCalls call) {
        if (call != null) {
            if (call.getBody() != null && call.getBody().getBodyId() != null) {
                throw new BadRequestException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(BODY_WITH_ID_SHOULD_NOT_BE_PROVIDED, FIRST_PLACEHOLDER, String.valueOf(call.getBody().getBodyId()))));
            }
            if (call.getHeaders() != null) {
                this.validateHeaders(call);
            }
            if (call.getParams() != null) {
                this.validateParams(call);
            }
            if (call.getParamsMultiCalls() != null) {
                this.validateParamsMultiCalls(call);
            }
        } else {
            throw new BadRequestException(StringUtils.EMPTY);
        }
    }


    /**
     * Add a new MspCalls
     *
     * @param call MspCalls object
     * @return MspCalls informations for the MspCalls added
     */
    @Override
    public MSPCalls addMspCall(MSPCalls call) {
        this.validateUuidNotProvided(call);
        try {
            return callRepository.save(call);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, call.getAction().getMspActionId().toString()));
        }
    }


    /**
     * Retrieve a list of MspCalls transported into MspCalls
     *
     * @return List of MspCalls
     */
    @Override
    public List<MSPCalls> getAllCalls() {
        return (List<MSPCalls>) callRepository.findAll();
    }


    /**
     * Update all the MspCalls informations
     *
     * @param id   Identifier of the MspCalls
     * @param call MspCalls object
     * @return MspCalls informations for the MspCalls puted
     */
    @Override
    public MSPCalls updateMspCall(UUID id, MSPCalls call) {
        Optional<MSPCalls> mspCallToBeUpdatedOptional = this.callRepository.findById(id);
        if (call != null && call.getAction().getMspActionId() != null) {
            if (call.getBody() != null) {
                if (call.getBody().getBodyId() == null && mspCallToBeUpdatedOptional.isPresent()) {
                    updateBodyOnMspCall(mspCallToBeUpdatedOptional.get());
                }
                if (call.getBody().getBodyParams() != null && !call.getBody().getBodyParams().isEmpty()) {
                    updateBodyParam(call);
                }
                this.bodyRepository.save(call.getBody());
            }
            call.setMspCallId(id);
            try {
                callRepository.save(call);
            } catch (Exception e) {
                throw new NotFoundException(CommonUtils.placeholderFormat(MSP_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, call.getAction().getMspActionId().toString()));
            }
        }
        return call;
    }

    /**
     * Delete a MspCalls
     *
     * @param id Identifier of the MspCalls
     */

    @Override
    public void deleteMspCall(UUID id) {
        try {
            callRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(MSP_CALLS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()));
        }
    }

    /**
     * Retrieve a MspCalls informations.
     *
     * @param id Identifier of the MspCalls
     * @return MspCalls informations for the MspCalls
     */
    @Override
    public MSPCalls findMspCallsById(UUID id) {
        return callRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_CALLS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));

    }

    /**
     * Get MspCalls from MspActions id
     *
     * @param id Identifier of the MspActions
     * @return MspCalls
     */
    @Override
    public List<MSPCalls> findByActionMspActionId(UUID id) {
        List<MSPCalls> mspCall = callRepository.findByActionMspActionId(id);
        if (mspCall == null || mspCall.isEmpty()) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_CALLS_WITH_MSP_ACTIONS_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return mspCall;
    }

    /**
     * Validate Headers to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call
     */
    private void validateHeaders(MSPCalls call) {
        for (Headers headers : call.getHeaders()) {
            if (headers != null && headers.getHeadersId() != null) {
                throw new BadRequestException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(HEADERS_WITH_ID_SHOULD_NOT_BE_PROVIDED, FIRST_PLACEHOLDER, String.valueOf(headers.getHeadersId()))));
            }
        }
    }

    /**
     * Validate Params to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call
     */
    private void validateParams(MSPCalls call) {
        if (call.getParams() != null && !call.getParams().isEmpty()) {
            for (Params params : call.getParams()) {
                if (params.getParamsId() != null) {
                    throw new BadRequestException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(PARAMS_WITH_ID, FIRST_PLACEHOLDER, String.valueOf(params.getParamsId()))));

                }
            }
        }
    }

    /**
     * Validate ParamsMultiCalls to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call
     */
    private void validateParamsMultiCalls(MSPCalls call) {
        if (call.getParamsMultiCalls() != null && !call.getParamsMultiCalls().isEmpty()) {
            for (ParamsMultiCalls paramsMultiCalls : call.getParamsMultiCalls()) {
                if (paramsMultiCalls.getParamsMultiCallsId() != null) {
                    throw new BadRequestException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(PARAMS_MULTI_CALLS_WITH_ID, FIRST_PLACEHOLDER, String.valueOf(paramsMultiCalls.getParamsMultiCallsId()))));
                }
            }
        }
    }

    /**
     * Update bodyParam informations
     * @param call mspCall object
     */
    private void updateBodyParam(MSPCalls call) {
        Set<BodyParams> newbodyParams = new HashSet<>();
        call.getBody().getBodyParams().forEach(bodyParam ->
                newbodyParams.add(this.bodyParamRepository.save(bodyParam)));
        call.getBody().setBodyParams(newbodyParams);
    }

    /**
     * Update body informations
     * @param call mspCall object
     */
    private void updateBodyOnMspCall(MSPCalls call) {
        Body bodyToBeDeleted = call.getBody();
        if (bodyToBeDeleted != null && bodyToBeDeleted.getBodyId() != null) {
            call.setBody(null);
            this.callRepository.save(call);
        }
    }

}
