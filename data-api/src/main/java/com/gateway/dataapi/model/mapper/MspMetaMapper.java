package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.database.model.MspMeta;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface MspMetaMapper {

    /**
     * Convert database entity MspMeta to MspMetaDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    MspMetaDTO mapEntityToDto(MspMeta source);

    /**
     * Convert database entity List MspMeta to List MspMetaDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<MspMetaDTO> mapEntityToDto(List<MspMeta> source);

    /**
     * Convert List MspMetaDTO to database List MspMeta entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<MspMeta> mapDtoToEntity(List<MspMetaDTO> source);

    /**
     * Convert database entity MspMeta to MspMetaDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    MspMeta mapDtoToEntity(MspMetaDTO source);
}
