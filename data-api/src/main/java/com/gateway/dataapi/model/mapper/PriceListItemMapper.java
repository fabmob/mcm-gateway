package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PriceListItemDTO;
import com.gateway.database.model.PriceListItem;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface PriceListItemMapper {
    /**
     * Convert database entity PriceListItem to PriceListItemDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PriceListItemDTO mapEntityToDto(PriceListItem source);

    /**
     * Convert database entity List PriceListItem to List PriceListItemDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PriceListItemDTO> mapEntityToDto(List<PriceListItem> source);

    /**
     * Convert List PriceListItemDTO to database List PriceListItem entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PriceListItem> mapDtoToEntity(List<PriceListItemDTO> source);

    /**
     * Convert database entity PriceListItem to PriceListItemDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PriceListItem mapDtoToEntity(PriceListItemDTO source);
}
