package com.gateway.api.model.mapper;

import com.gateway.api.model.Duration;
import com.gateway.commonapi.dto.data.DurationDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map DTO from datab-api and DTO from api.
 */
@Mapper
public interface DurationDTOMapper {
    /**
     * Convert database entity Duration to DurationDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    Duration mapDataApiDtoToApiDto(DurationDTO source);

    /**
     * Convert database entity List Duration to List DurationDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<Duration> mapDataApiDtoToApiDto(List<DurationDTO> source);

    /**
     * Convert List DurationDTO to database List Duration entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<DurationDTO> mapApiDtoToDataApiDto (List<Duration> source);

    /**
     * Convert database entity Duration to DurationDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    DurationDTO mapApiDtoToDataApiDto (Duration source);


}
