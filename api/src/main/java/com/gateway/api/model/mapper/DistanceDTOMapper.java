package com.gateway.api.model.mapper;

import com.gateway.api.model.Distance;
import com.gateway.commonapi.dto.data.DistanceDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface DistanceDTOMapper {
    /**
     * Convert database entity Distance to DistanceDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    Distance mapDataApiDtoToApiDto(DistanceDTO source);

    /**
     * Convert database entity List Distance to List DistanceDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<Distance> mapDataApiDtoToApiDto(List<DistanceDTO> source);

    /**
     * Convert List DistanceDTO to database List Distance entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<DistanceDTO> mapApiDtoToDataApiDto(List<Distance> source);

    /**
     * Convert database entity Distance to DistanceDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    DistanceDTO mapApiDtoToDataApiDto(Distance source);
}
