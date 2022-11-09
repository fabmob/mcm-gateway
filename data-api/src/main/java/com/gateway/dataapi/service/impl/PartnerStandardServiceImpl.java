package com.gateway.dataapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.model.mapper.PartnerStandardMapper;
import com.gateway.dataapi.service.PartnerStandardService;
import com.gateway.database.model.PartnerActions;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.model.PartnerStandard;
import com.gateway.database.service.PartnerStandardDatabaseService;
import com.gateway.database.service.impl.PartnerActionsDatabaseServiceImpl;
import com.gateway.database.service.impl.PartnerMetaDatabaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gateway.commonapi.utils.enums.ActionsEnum.*;
import static com.gateway.dataapi.util.constant.DataApiMessageDict.*;
import static com.gateway.database.util.constant.DataMessageDict.*;

@Service
@Slf4j
public class PartnerStandardServiceImpl implements PartnerStandardService {


    @Autowired
    private PartnerStandardDatabaseService partnerStandardDatabaseService;

    @Autowired
    PartnerActionsDatabaseServiceImpl partnerActionsDatabaseService;

    @Autowired
    PartnerMetaDatabaseServiceImpl partnerMetaDatabaseService;

    private final PartnerStandardMapper mapper = Mappers.getMapper(PartnerStandardMapper.class);


    @Override
    public PartnerStandardDTO addPartnerStandard(PartnerStandardDTO standardDTO) throws NotFoundException {
        checkData(standardDTO);
        PartnerStandard standard = mapper.mapDtoToEntity(standardDTO);
        checkCompatibility(standard);
        return mapper.mapEntityToDto(partnerStandardDatabaseService.addPartnerStandard(standard));
    }

    @Override
    public void updatePartnerStandard(UUID id, PartnerStandardDTO standardDTO) {
        this.checkData(standardDTO);
        if (log.isDebugEnabled()) {
            try {
                log.debug(new ObjectMapper().writeValueAsString(standardDTO));
            } catch (Exception e) {
                throw new UnsupportedOperationException(UNABLE_TO_CONVERT_OBJECT_TO_JSON);
            }
        }
        PartnerStandard standard = mapper.mapDtoToEntity(standardDTO);
        checkCompatibility(standard);
        partnerStandardDatabaseService.updatePartnerStandard(id, standard);
    }

    @Override
    public void deletePartnerStandard(UUID id) {
        partnerStandardDatabaseService.deletePartnerStandard(id);
    }

    /**
     * Retrieve a PartnerStandardDto information.
     *
     * @param id Identifier of the PartnerStandardDto
     * @return PartnerStandardDto information for the PartnerStandardDto
     * @throws NotFoundException not found object
     */
    @Override
    public PartnerStandardDTO getPartnerStandardFromId(UUID id) throws NotFoundException {
        return mapper.mapEntityToDto(partnerStandardDatabaseService.findPartnerStandardById(id));
    }


    /**
     * Get PartnerStandardDto from PartnerActions id and PartnerMeta id VersionStandard
     *
     * @param partnerMetaId      Partner ID
     * @param partnerActionsName Action Name
     * @param versionStandard    Standard version
     * @param versionDatamapping Datamapping version
     * @return PartnerStandardDto
     * @throws NotFoundException not found
     */
    @Override
    public List<PartnerStandardDTO> getByCriteria(UUID partnerMetaId, UUID partnerActionsId, String partnerActionsName, String versionStandard, String versionDatamapping, Boolean isActive) throws NotFoundException {
        return mapper.mapEntityToDto(partnerStandardDatabaseService.getByCriteria(partnerMetaId, partnerActionsId, partnerActionsName, versionStandard, versionDatamapping, isActive));
    }

    @Override
    public List<PartnerStandardDTO> getAllPartnerStandards() {
        List<PartnerStandard> standards = partnerStandardDatabaseService.getAllPartnerStandard();
        return mapper.mapEntityToDto(standards);
    }

    /**
     * check data the PK if not null
     *
     * @param partnerStandardDTO body of the request
     */
    private void checkData(PartnerStandardDTO partnerStandardDTO) {
        if (partnerStandardDTO.getPartnerActionsId() == null) {
            throw new BadRequestException(ACTION_ID_IS_NULL);
        }
        if (partnerStandardDTO.getPartnerId() == null) {
            throw new BadRequestException(PARTNER_ID_IS_NULL);
        }
        if (partnerStandardDTO.getAdaptersId() == null) {
            throw new BadRequestException(ADAPTER_ID_IS_NULL);
        }
        if (partnerStandardDTO.getVersionDataMapping() == null) {
            throw new BadRequestException(VERSION_DATA_MAPPING_IS_NULL);
        }
        if (partnerStandardDTO.getVersionStandard() == null) {
            throw new BadRequestException(VERSION_STANDARD_IS_NULL);
        }
    }

    /**
     * Check if the standard action is compatible with the partner, throws ConflictException if not.
     *
     * @param standard
     */
    private void checkCompatibility(PartnerStandard standard) {
        if (standard.getId() != null && standard.getId().getPartner() != null && standard.getId().getAction() != null) {
            PartnerMeta partnerMetaInDB = partnerMetaDatabaseService.findPartnerMetaById(standard.getId().getPartner().getPartnerId());
            PartnerActions partnerActions = partnerActionsDatabaseService.findPartnerActionById(standard.getId().getAction().getPartnerActionId());


            Map<String, Boolean> supportedFeatures = new HashMap<>();
            supportedFeatures.put(ASSET_SEARCH.value, partnerMetaInDB.getHasVehicle());
            supportedFeatures.put(AVAILABLE_ASSET_SEARCH.value, partnerMetaInDB.getHasVehicle());
            supportedFeatures.put(STATION_SEARCH.value, partnerMetaInDB.getHasStation());
            supportedFeatures.put(STATION_STATUS_SEARCH.value, partnerMetaInDB.getHasStationStatus());
            supportedFeatures.put(MSP_ZONE_SEARCH.value, partnerMetaInDB.getHasOperatingZone());
            supportedFeatures.put(PARKING_SEARCH.value, partnerMetaInDB.getHasParking());
            supportedFeatures.put(VEHICLE_TYPES_SEARCH.value, partnerMetaInDB.getHasVehicleTypes());
            supportedFeatures.put(PRICING_PLAN_SEARCH.value, partnerMetaInDB.getHasPricingPlan());
            supportedFeatures.put(DRIVER_JOURNEYS_SEARCH.value, partnerMetaInDB.getHasCarpoolingDriverJourney());
            supportedFeatures.put(PASSENGER_JOURNEYS_SEARCH.value, partnerMetaInDB.getHasCarpoolingPassengerJourney());
            supportedFeatures.put(PASSENGER_REGULAR_TRIPS_SEARCH.value, partnerMetaInDB.getHasCarpoolingPassengerTrip());
            supportedFeatures.put(DRIVER_REGULAR_TRIPS_SEARCH.value, partnerMetaInDB.getHasCarpoolingDriverTrip());
            supportedFeatures.put(CARPOOLING_BOOK.value, partnerMetaInDB.getHasCarpoolingBookingPost());
            supportedFeatures.put(CARPOOLING_PATCH_BOOKING.value, partnerMetaInDB.getHasCarpoolingBookingPatch());
            supportedFeatures.put(CARPOOLING_BOOKING_SEARCH.value, partnerMetaInDB.getHasCarpoolingBookingGet());
            supportedFeatures.put(SEND_MESSAGE.value, partnerMetaInDB.getHasCarpoolingMessages());
            supportedFeatures.put(STATUS.value, partnerMetaInDB.getHasCarpoolingStatus());
            supportedFeatures.put(PING.value, partnerMetaInDB.getHasPing());
            supportedFeatures.put(AROUND_ME_SEARCH.value, partnerMetaInDB.getHasAroundMe());
            supportedFeatures.put(BOOKING_EVENT.value, partnerMetaInDB.getHasCarpoolingBookingEvent());


            if (!partnerActions.isAuthentication() && !Boolean.TRUE.equals(supportedFeatures.get(partnerActions.getAction()))) {
                throw new ConflictException(CommonUtils.placeholderFormat(THE_MSP_DOES_NOT_HANDLE_THIS_FEATURE, FIRST_PLACEHOLDER, partnerMetaInDB.getPartnerId().toString(), SECOND_PLACEHOLDER, partnerActions.getAction()));
            }
        }
    }


}