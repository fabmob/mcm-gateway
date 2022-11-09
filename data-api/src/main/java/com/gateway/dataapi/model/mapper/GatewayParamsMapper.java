package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.database.model.GatewayParams;
import org.mapstruct.Mapper;

import java.util.List;

;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface GatewayParamsMapper {

    /**
     * Convert database entity GatewayParams to GatewayParamsDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    GatewayParamsDTO mapEntityToDto(GatewayParams source);

    /**
     * Convert database entity List GatewayParams to List GatewayParamsDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<GatewayParamsDTO> mapEntityToDto(List<GatewayParams> source);

    /**
     * Convert List GatewayParamsDTO to database List GatewayParams entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<GatewayParams> mapDtoToEntity(List<GatewayParamsDTO> source);

    /**
     * Convert database entity GatewayParams to GatewayParamsDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    GatewayParams mapDtoToEntity(GatewayParamsDTO source);
}
