package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.MspStandard;
import com.gateway.database.model.StandardPK;
import com.gateway.database.repository.MspStandardRepository;
import com.gateway.database.service.MspStandardDatabaseService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class MspStandardDatabaseServiceImpl implements MspStandardDatabaseService {

    @Autowired
    MspStandardRepository mspStandardRepository;

    @Autowired
    MSPActionsDatabaseServiceImpl mspActionsDatabaseService;

    @Autowired
    MspMetaDatabaseServiceImpl mspMetaDatabaseService;

    @Autowired
    AdaptersDatabaseServiceImpl adaptersDatabaseService;


    @Autowired
    private ErrorMessages errorMessages;


    /**
     * Add a new MspStandard
     *
     * @param standard MspStandard object
     * @return MspStandard information for the MspStandard added
     */

    @Override
    public MspStandard addMspStandard(MspStandard standard) {
        if (this.findMspStandardByPK(standard.getId()).isPresent()) {
            throw new ConflictException(CommonUtils.placeholderFormat(THERE_IS_ALREADY_MSP_STANDARD_FOUND,
                    FIRST_PLACEHOLDER, String.valueOf(standard.getId().getMsp().getMspId()),
                    SECOND_PLACEHOLDER, String.valueOf(standard.getId().getAction().getMspActionId()),
                    THIRD_PLACEHOLDER, standard.getId().getVersionDataMapping(),
                    FOURTH_PLACEHOLDER, standard.getId().getVersionStandard()));
        } else {
            //Check if datas exist in db, will throw a NotFoundException with corresponding error if not
            try {
                mspMetaDatabaseService.findMspMetaById(standard.getId().getMsp().getMspId());
                mspActionsDatabaseService.findMspActionById(standard.getId().getAction().getMspActionId());
                adaptersDatabaseService.findAdapterById(standard.getAdapter().getAdapterId());
                standard.setMspStandardId(UUID.randomUUID());
                return mspStandardRepository.save(standard);
            } catch (Exception e) {
                throw new NotFoundException(e.getMessage());
            }
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
     * Update all the MspStandard information
     *
     * @param id       Identifier of the MspStandard
     * @param standard MspStandard object
     * @return MspStandard information for the MspStandard put
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
        mspStandardRepository.deleteById(mspStandardFound.getId());
    }

    /**
     * Retrieve a MspStandard information.
     *
     * @param id Identifier of the MspStandard
     * @return MspStandard information for the MspStandard
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
     * Find by PK
     *
     * @param standardPK PK
     * @return Optional empty if not found or Optional MspStandard if found
     */
    @Override
    public Optional<MspStandard> findMspStandardByPK(StandardPK standardPK) {
        return mspStandardRepository.findById(standardPK);
    }

    /**
     * Get MspStandard from MspMeta id And MspActions id VersionStandard VersionDatamapping
     *
     * @param mspMetaId          MSP ID
     * @param mspActionsName     Action Name
     * @param versionStandard    Standard Version
     * @param versionDatamapping Datamapping version
     * @return liste of MspStandard
     */
    @Override
    public List<MspStandard> getByCriteria(UUID mspMetaId, UUID mspActionsId, String mspActionsName, String
            versionStandard, String versionDatamapping, Boolean isActive) {
        List<MspStandard> mspStandards = mspStandardRepository.findByKeyPrimary(mspMetaId, mspActionsId, mspActionsName, versionStandard, versionDatamapping, isActive);
        if (mspStandards == null || mspStandards.isEmpty()) {
            String mspMetaIdValue = mspMetaId != null ? mspMetaId.toString() : null;
            String mspActionsIdValue = mspActionsId != null ? mspActionsId.toString() : null;
            throw new NotFoundException(MessageFormat.format(MSP_STANDARD_WITH_CRITERIA_NOT_FOUND, mspMetaIdValue, mspActionsIdValue, mspActionsName, versionStandard, versionDatamapping, isActive));
        }
        return mspStandards;
    }

    @Override
    public List<MspStandard> findAllByAdapterId(UUID adapterId){
        return mspStandardRepository.findByAdapterId(adapterId);
    }

}
