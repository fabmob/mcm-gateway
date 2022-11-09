package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.PartnerStandard;
import com.gateway.database.model.StandardPK;
import com.gateway.database.repository.PartnerStandardRepository;
import com.gateway.database.service.PartnerStandardDatabaseService;
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
public class PartnerStandardDatabaseServiceImpl implements PartnerStandardDatabaseService {

    @Autowired
    PartnerStandardRepository partnerStandardRepository;

    @Autowired
    PartnerActionsDatabaseServiceImpl partnerActionsDatabaseService;

    @Autowired
    PartnerMetaDatabaseServiceImpl partnerMetaDatabaseService;

    @Autowired
    AdaptersDatabaseServiceImpl adaptersDatabaseService;


    @Autowired
    private ErrorMessages errorMessages;


    /**
     * Add a new PartnerStandard
     *
     * @param standard PartnerStandard object
     * @return PartnerStandard information for the PartnerStandard added
     */

    @Override
    public PartnerStandard addPartnerStandard(PartnerStandard standard) {
        //-------Check if the Standard PK already exist in DB and throw exception if true------//
        Optional<PartnerStandard> checkingPartnerStandard = this.findPartnerStandardByPK(standard.getId());
        if (checkingPartnerStandard.isPresent()) {
            throw new ConflictException(CommonUtils.placeholderFormat(THERE_IS_ALREADY_PARTNER_STANDARD_FOUND,
                    FIRST_PLACEHOLDER, String.valueOf(standard.getId().getPartner().getPartnerId()),
                    SECOND_PLACEHOLDER, String.valueOf(standard.getId().getAction().getPartnerActionId()),
                    THIRD_PLACEHOLDER, standard.getId().getVersionDataMapping(),
                    FOURTH_PLACEHOLDER, standard.getId().getVersionStandard()));
        } else {
            //Check if partnerMeta and partnerActions exist in db and check compatibility, will throw an Exception with corresponding error if not
            try {
                partnerMetaDatabaseService.findPartnerMetaById(standard.getId().getPartner().getPartnerId());
                partnerActionsDatabaseService.findPartnerActionById(standard.getId().getAction().getPartnerActionId());
                adaptersDatabaseService.findAdapterById(standard.getAdapter().getAdapterId());
                standard.setPartnerStandardId(UUID.randomUUID());
                return partnerStandardRepository.save(standard);
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            } catch (Exception e) {
                throw new InternalException(SOMETHING_WENT_WRONG_DURING_SAVE_OPERATION);
            }
        }
    }

    /**
     * Retrieve a list of PartnerStandard transported into PartnerStandard
     *
     * @return List of PartnerStandard
     */
    @Override
    public List<PartnerStandard> getAllPartnerStandard() {
        return (List<PartnerStandard>) partnerStandardRepository.findAll();
    }


    /**
     * Update all the PartnerStandard information
     *
     * @param id       Identifier of the PartnerStandard
     * @param standard PartnerStandard object
     * @return PartnerStandard information for the PartnerStandard put
     */
    @Override
    public PartnerStandard updatePartnerStandard(UUID id, PartnerStandard standard) {
        try {
            standard.setPartnerStandardId(id);
            adaptersDatabaseService.findAdapterById(standard.getAdapter().getAdapterId());
            this.deletePartnerStandard(id);
            partnerStandardRepository.save(standard);
            return standard;
        } catch (NotFoundException e) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        } catch (Exception e) {
            throw new InternalException(SOMETHING_WENT_WRONG_DURING_SAVE_OPERATION);
        }
    }

    /**
     * Delete a PartnerStandard
     *
     * @param id Identifier of the PartnerStandard
     */
    @Override
    public void deletePartnerStandard(UUID id) {
        PartnerStandard partnerStandardFound = partnerStandardRepository.findByPartnerStandardId(id);
        if (partnerStandardFound == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        partnerStandardRepository.deleteById(partnerStandardFound.getId());
    }

    /**
     * Retrieve a PartnerStandard information.
     *
     * @param id Identifier of the PartnerStandard
     * @return PartnerStandard information for the PartnerStandard
     */
    @Override
    public PartnerStandard findPartnerStandardById(UUID id) {
        PartnerStandard partnerStandardFound = partnerStandardRepository.findByPartnerStandardId(id);
        if (partnerStandardFound == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_STANDARD_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
        }
        return partnerStandardFound;

    }

    /**
     * Find by PK
     *
     * @param standardPK PK
     * @return Optional empty if not found or Optional PartnerStandard if found
     */
    @Override
    public Optional<PartnerStandard> findPartnerStandardByPK(StandardPK standardPK) {
        return partnerStandardRepository.findById(standardPK);
    }

    /**
     * Get PartnerStandard from PartnerMeta id And PartnerActions id VersionStandard VersionDatamapping
     *
     * @param partnerId          Partner ID
     * @param partnerActionsName Action Name
     * @param versionStandard    Standard Version
     * @param versionDatamapping Datamapping version
     * @return list of PartnerStandard
     */
    @Override
    public List<PartnerStandard> getByCriteria(UUID partnerId, UUID partnerActionsId, String partnerActionsName, String
            versionStandard, String versionDatamapping, Boolean isActive) {
        List<PartnerStandard> partnerStandards = partnerStandardRepository.findByKeyPrimary(partnerId, partnerActionsId, partnerActionsName, versionStandard, versionDatamapping, isActive);
        if (partnerStandards == null || partnerStandards.isEmpty()) {
            String partnerMetaIdValue = partnerId != null ? partnerId.toString() : null;
            String partnerActionsIdValue = partnerActionsId != null ? partnerActionsId.toString() : null;
            throw new NotFoundException(MessageFormat.format(PARTNER_STANDARD_WITH_CRITERIA_NOT_FOUND, partnerMetaIdValue, partnerActionsIdValue, partnerActionsName, versionStandard, versionDatamapping, isActive));
        }
        return partnerStandards;
    }

    @Override
    public List<PartnerStandard> findAllByAdapterId(UUID adapterId) {
        return partnerStandardRepository.findByAdapterId(adapterId);
    }

}
