package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.database.model.PriceList;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */

@Mapper
public interface PriceListMapper {
    /**
     * Convert database entity PriceList to PriceListDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PriceListDTO mapEntityToDto(PriceList source);

    /**
     * Convert database entity List PriceList to List PriceListDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PriceListDTO> mapEntityToDto(List<PriceList> source);

    /**
     * Convert List PriceListDTO to database List PriceList entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PriceList> mapDtoToEntity(List<PriceListDTO> source);

    /**
     * Convert database entity PriceList to PriceListDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PriceList mapDtoToEntity(PriceListDTO source);
}
