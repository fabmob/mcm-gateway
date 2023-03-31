package com.gateway.api.service.partnerservice;

import com.gateway.api.model.PartnerMeta;
import com.gateway.commonapi.dto.api.PartnerZone;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.PartnerTypeRequestHeader;
import com.gateway.commonapi.utils.enums.ZoneType;

import java.util.List;
import java.util.UUID;


public interface PartnerService {

    /**
     * Retrieve PartnerZone with ZoneType
     *
     * @param partnerId Identifier of the Partner.
     * @param areaType  Type of area
     * @return PartnerZone
     */
    PartnerZone getPartnerZone(UUID partnerId, ZoneType areaType);

    /**
     * Retrieve a list of Partners metadata.
     *
     * @return List of {@link PartnerMeta} Partners metadata.
     */
    List<PartnerMeta> getPartnersMeta();

    /**
     * Will call data-api to filter on the example given and corresponds to all given attributs
     *
     * @param partnerMetaExample give an example of PartnerMeta
     * @param callPartnerType    partner type of the caller MSP ? MAAS ? ADMIN ?
     * @return a list of PartneMeta that correspond to the example
     */
    List<PartnerMeta> getPartnersMetaByExample(PartnerMeta partnerMetaExample, PartnerTypeRequestHeader callPartnerType);

    /**
     * Retrieve a list of Partners metadata using partner type (MaaS, MSP).
     *
     * @return List of {@link PartnerMeta} Partners metadata.
     */


    List<PartnerMeta> getPartnersMetaByPartnerType(PartnerTypeEnum partnerType, PartnerTypeRequestHeader callPartnerType);

    /**
     * Retrieve a Partner metadata information.
     *
     * @param partnerId Identifier of the Partner.
     * @return {@link PartnerMeta} Metadata information for the Partner
     * @throws NotFoundException PartnerMeta not found
     */
    PartnerMeta getPartnerMeta(UUID partnerId);

    /**
     * Give health status of the webservice of MSP following "Carpool" Standard
     *
     * @param mspId Identifier of the Partner.
     */
    void getStatus(UUID mspId);

    /**
     * Give health status of the webservice of MSP following "TOMP" Standard
     *
     * @param mspId Identifier of the Partner.
     */
    void ping(UUID mspId);


}

