package com.gateway.api.service.mspservice;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.api.model.MSPMeta;
import com.gateway.commonapi.dto.api.MSPZone;
import com.gateway.commonapi.utils.enums.ZoneType;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


public interface MSPService {

    /**
     * Retrieve mspZone with ZoneType 
     * @param mspId
     * @param areaType
     * @return MSPZone
     */
    MSPZone getMSPZone(UUID mspId, ZoneType areaType) ;

    /**
     * Retrieve a list of MSPs metadatas.
     *
     * @return List of {@link MSPMeta} MSPs metadatas.
     */
    List<MSPMeta> getMSPsMeta() throws IOException, InterruptedException;

    /**
     * Retrieve a MSP metadatas informations.
     *
     * @param mspId Identifier of the MSP.
     * @return {@link MSPMeta} Metadata informations for the MSP
     * @throws NotFoundException mspMeta not found
     */
    MSPMeta getMSPMeta(UUID mspId) ;

}

