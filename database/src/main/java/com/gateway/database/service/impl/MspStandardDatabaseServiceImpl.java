package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.MspStandard;
import com.gateway.database.repository.MSPActionsRepository;
import com.gateway.database.repository.MspStandardRepository;
import com.gateway.database.service.MspStandardDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;


@Service
public class MspStandardDatabaseServiceImpl implements MspStandardDatabaseService {

    @Autowired
    MspStandardRepository mspStandardRepository;

    @Autowired
    MSPActionsRepository mspActionsRepositoty;

    @Autowired
    private ErrorMessages errorMessages;

    public MspStandardDatabaseServiceImpl(MspStandardRepository mspStandardRepository,
                                          MSPActionsRepository mspActionsRepositoty) {
        super();
        this.mspStandardRepository = mspStandardRepository;
        this.mspActionsRepositoty = mspActionsRepositoty;
    }

    public MspStandardDatabaseServiceImpl() {
    }

    /**
     * Add a new MspStandard
     *
     * @param standard MspStandard object
     * @return MspStandard informations for the MspStandard added
     */

    @Override
    public MspStandard addMspStandard(MspStandard standard) {
        try {
            standard.setMspStandardId(UUID.randomUUID());
            return mspStandardRepository.save(standard);
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(MSP_STANDARD_WITH_MSP_META_ID_MSP_ACTIONS_ID_IS_NOT_FOUND, standard.getId().getMsp().getMspId().toString(), standard.getId().getAction().getMspActionId().toString()));
        }
    }

    /**
     * Retrieve a list of MspStandard transported into MspStandard
     *
     * @return List of MspStandard
     */
    @Override
    public List<MspStandard> getAllMspStandard() {
        return (List<MspStandard>) mspStandardRepository.findAll();
    }


    /**
     * Update all the MspStandard informations
     *
     * @param id       Identifier of the MspStandard
     * @param standard MspStandard object
     * @return MspStandard informations for the MspStandard puted
     */
    @Override
    public MspStandard updateMspStandard(UUID id, MspStandard standard) {
        try {
            this.deleteMspStandard(id);
            standard.setMspStandardId(id);
            mspStandardRepository.save(standard);
            return standard;
        } catch (Exception e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));

        }
    }

    /**
     * Delete a MspStandard
     *
     * @param id Identifier of the MspStandard
     */
    @Override
    public void deleteMspStandard(UUID id) {
        MspStandard mspStandardFound = mspStandardRepository.findByMspStandardId(id);
        if (mspStandardFound == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        this.mspStandardRepository.deleteById(mspStandardFound.getId());
    }

    /**
     * Retrieve a MspStandard informations.
     *
     * @param id Identifier of the MspStandard
     * @return MspStandard informations for the MspStandard
     */
    @Override
    public MspStandard findMspStandardById(UUID id) {
        MspStandard mspStandardFound = mspStandardRepository.findByMspStandardId(id);
        if (mspStandardFound == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return mspStandardFound;

    }

    /**
     * Get MspStandard from MspMeta id And MspActions id VersionStandard VersionDatamapping
     *
     * @param mspMetaId
     * @param mspActionsId
     * @param versionStandard
     * @param versionDatamapping
     * @return liste of MspStandard
     */
    @Override
    public List<MspStandard> getByCriteria(UUID mspMetaId, UUID mspActionsId, String versionStandard, String versionDatamapping) {
        List<MspStandard> mspStandards = mspStandardRepository.findByKeyPrimary(mspMetaId, mspActionsId, versionStandard, versionDatamapping);
        if (mspStandards == null || mspStandards.isEmpty()) {
            String mspMetaIdValue = mspMetaId != null ? mspMetaId.toString() : null;
            String mspActionsIdValue = mspActionsId != null ? mspActionsId.toString() : null;
            throw new NotFoundException(MessageFormat.format(MSP_STANDARD_WITH_CRITERIA_NOT_FOUND, mspMetaIdValue, mspActionsIdValue, versionStandard, versionDatamapping));
        }
        return mspStandards;
    }

}
