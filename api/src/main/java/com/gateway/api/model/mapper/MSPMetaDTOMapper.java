package com.gateway.api.model.mapper;

import com.gateway.api.model.MSPMeta;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import org.mapstruct.Mapper;

import java.util.List;
/**
 * Class using mapstruct to Map DTO from data-api and DTO from api .
 */
@Mapper
public interface MSPMetaDTOMapper {

    /**
     * Convert data-api MspMetaDTO to api DTO MSPMeta
     * @param source entity object to map
     * @return dto result of the mapping
     */
    MSPMeta mapDataApiDtoToApiDto(MspMetaDTO source);

    /**
     * Convert List of data-api MspMetaDTO to List of api DTO MSPMeta
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<MSPMeta> mapDataApiDtoToApiDto(List<MspMetaDTO> source);

    /**
     * Convert List MspMetaDTO to database List MspMeta entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<MspMetaDTO> mapApiDtoToDataApiDto(List<MSPMeta> source);

    /**
     * Convert database entity MspMeta to MspMetaDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    MspMetaDTO mapApiDtoToDataApiDto (MSPMeta source);

}

