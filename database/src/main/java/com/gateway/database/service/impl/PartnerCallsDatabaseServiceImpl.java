package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.*;
import com.gateway.database.repository.BodyParamsRepository;
import com.gateway.database.repository.BodyRepository;
import com.gateway.database.repository.PartnerCallsRepository;
import com.gateway.database.service.PartnerCallsDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Slf4j
@Service
public class PartnerCallsDatabaseServiceImpl implements PartnerCallsDatabaseService {

    @Autowired
    private PartnerCallsRepository callRepository;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private BodyParamsRepository bodyParamRepository;

    @Autowired
    private ErrorMessages errorMessages;


    public PartnerCallsDatabaseServiceImpl(PartnerCallsRepository callRepo, BodyRepository bodyRepo,
                                           BodyParamsRepository bodyParamRepo) {
        super();
        this.callRepository = callRepo;
        this.bodyRepository = bodyRepo;
        this.bodyParamRepository = bodyParamRepo;
    }

    public PartnerCallsDatabaseServiceImpl() {
    }

    public BodyRepository getBodyRepository() {
        return bodyRepository;
    }

    /**
     * Validate Body to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call the call in which we check id UUID is present
     */
    private void validateUuidNotProvided(PartnerCalls call) {
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
     * Add a new PartnerCalls
     *
     * @param call PartnerCalls object
     * @return PartnerCalls information for the PartnerCalls added
     */
    @Override
    public PartnerCalls addPartnerCall(PartnerCalls call) {
        this.validateUuidNotProvided(call);
        try {
            return callRepository.save(call);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException(CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, call.getAction().getPartnerActionId().toString()));
        }
    }


    /**
     * Retrieve a list of PartnerCalls transported into PartnerCalls
     *
     * @return List of PartnerCalls
     */
    @Override
    public List<PartnerCalls> getAllCalls() {
        return (List<PartnerCalls>) callRepository.findAll();
    }


    /**
     * Update all the PartnerCalls information
     *
     * @param id   Identifier of the PartnerCalls
     * @param call PartnerCalls object
     * @return PartnerCalls information for the PartnerCalls put
     */
    @Override
    public PartnerCalls updatePartnerCall(UUID id, PartnerCalls call) {
        Optional<PartnerCalls> partnerCallToBeUpdatedOptional = this.callRepository.findById(id);
        if (call != null && call.getAction().getPartnerActionId() != null) {
            if (call.getBody() != null) {
                if (call.getBody().getBodyId() == null && partnerCallToBeUpdatedOptional.isPresent()) {
                    updateBodyOnPartnerCall(partnerCallToBeUpdatedOptional.get());
                }
                if (call.getBody().getBodyParams() != null && !call.getBody().getBodyParams().isEmpty()) {
                    updateBodyParam(call);
                }
                this.bodyRepository.save(call.getBody());
            }
            call.setPartnerCallId(id);
            try {
                callRepository.save(call);
            } catch (Exception e) {
                throw new NotFoundException(CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, call.getAction().getPartnerActionId().toString()));
            }
        }
        return call;
    }

    /**
     * Delete a PartnerCalls
     *
     * @param id Identifier of the PartnerCalls
     */

    @Override
    public void deletePartnerCall(UUID id) {
        try {
            callRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(PARTNER_CALLS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()));
        }
    }

    /**
     * Retrieve a PartnerCalls information.
     *
     * @param id Identifier of the PartnerCalls
     * @return PartnerCalls information for the PartnerCalls
     */
    @Override
    public PartnerCalls findPartnerCallsById(UUID id) {
        return callRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_CALLS_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));

    }

    /**
     * Get PartnerCalls from PartnerActions id
     *
     * @param id Identifier of the PartnerActions
     * @return PartnerCalls
     */
    @Override
    public List<PartnerCalls> findByActionPartnerActionId(UUID id) {
        List<PartnerCalls> partnerCall = callRepository.findByActionPartnerActionId(id);
        if (partnerCall == null || partnerCall.isEmpty()) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_CALLS_WITH_PARTNER_ACTIONS_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return partnerCall;
    }

    /**
     * Validate Headers to make sure there is no UUID inside otherwise throw BadRequestException
     *
     * @param call
     */
    private void validateHeaders(PartnerCalls call) {
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
    private void validateParams(PartnerCalls call) {
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
    private void validateParamsMultiCalls(PartnerCalls call) {
        if (call.getParamsMultiCalls() != null && !call.getParamsMultiCalls().isEmpty()) {
            for (ParamsMultiCalls paramsMultiCalls : call.getParamsMultiCalls()) {
                if (paramsMultiCalls.getParamsMultiCallsId() != null) {
                    throw new BadRequestException(MessageFormat.format(errorMessages.getTechnicalNotProvidedDescription(), CommonUtils.placeholderFormat(PARAMS_MULTI_CALLS_WITH_ID, FIRST_PLACEHOLDER, String.valueOf(paramsMultiCalls.getParamsMultiCallsId()))));
                }
            }
        }
    }

    /**
     * Update bodyParam information
     *
     * @param call PartnerCall object
     */
    private void updateBodyParam(PartnerCalls call) {
        Set<BodyParams> newbodyParams = new HashSet<>();
        call.getBody().getBodyParams().forEach(bodyParam ->
                newbodyParams.add(this.bodyParamRepository.save(bodyParam)));
        call.getBody().setBodyParams(newbodyParams);
    }

    /**
     * Update body information
     *
     * @param call PartnerCall object
     */
    private void updateBodyOnPartnerCall(PartnerCalls call) {
        Body bodyToBeDeleted = call.getBody();
        if (bodyToBeDeleted != null && bodyToBeDeleted.getBodyId() != null) {
            call.setBody(null);
            this.callRepository.save(call);
        }
    }

}
