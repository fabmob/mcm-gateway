package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.database.model.PartnerMeta;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface PartnerMetaMapper {

    /**
     * Convert database entity PartnerMeta to PartnerMetaDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PartnerMetaDTO mapEntityToDto(PartnerMeta source);

    /**
     * Convert database entity List PartnerMeta to List PartnerMetaDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PartnerMetaDTO> mapEntityToDto(List<PartnerMeta> source);

    /**
     * Convert List PartnerMetaDTO to database List PartnerMeta entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PartnerMeta> mapDtoToEntity(List<PartnerMetaDTO> source);

    /**
     * Convert database entity PartnerMeta to PartnerMetaDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PartnerMeta mapDtoToEntity(PartnerMetaDTO source);
}
