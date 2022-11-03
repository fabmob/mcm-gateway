package com.gateway.api.model.mapper;

import com.gateway.api.model.PartnerMeta;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import org.mapstruct.Mapper;

import java.util.List;
/**
 * Class using mapstruct to Map DTO from data-api and DTO from api .
 */
@Mapper
public interface PartnerMetaDTOMapper {

    /**
     * Convert data-api MspMetaDTO to api DTO MSPMeta
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PartnerMeta mapDataApiDtoToApiDto(PartnerMetaDTO source);

    /**
     * Convert List of data-api MspMetaDTO to List of api DTO MSPMeta
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PartnerMeta> mapDataApiDtoToApiDto(List<PartnerMetaDTO> source);

    /**
     * Convert List MspMetaDTO to database List MspMeta entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PartnerMetaDTO> mapApiDtoToDataApiDto(List<PartnerMeta> source);

    /**
     * Convert database entity MspMeta to MspMetaDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PartnerMetaDTO mapApiDtoToDataApiDto (PartnerMeta source);

}

