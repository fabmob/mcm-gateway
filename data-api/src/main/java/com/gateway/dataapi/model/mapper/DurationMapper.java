package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.DurationDTO;
import com.gateway.database.model.Duration;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface DurationMapper {
    /**
     * Convert database entity Duration to DurationDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    DurationDTO mapEntityToDto(Duration source);

    /**
     * Convert database entity List Duration to List DurationDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<DurationDTO> mapEntityToDto(List<Duration> source);

    /**
     * Convert List DurationDTO to database List Duration entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<Duration> mapDtoToEntity(List<DurationDTO> source);

    /**
     * Convert database entity Duration to DurationDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    Duration mapDtoToEntity(DurationDTO source);
}
