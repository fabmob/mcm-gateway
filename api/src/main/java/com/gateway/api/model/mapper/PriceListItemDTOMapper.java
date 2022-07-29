package com.gateway.api.model.mapper;

import com.gateway.api.model.PriceListItem;
import com.gateway.commonapi.dto.data.PriceListItemDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map entities from database and DTO from swagger interface.
 */
@Mapper
public interface PriceListItemDTOMapper {
    /**
     * Convert database entity PriceListItem to PriceListItemDTO
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PriceListItem mapDataApiDtoToApiDto(PriceListItemDTO source);

    /**
     * Convert database entity List PriceListItem to List PriceListItemDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PriceListItem>mapDataApiDtoToApiDto(List<PriceListItemDTO> source);

    /**
     * Convert List PriceListItemDTO to database List PriceListItem entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PriceListItemDTO> mapApiDtoToDataApiDto(List<PriceListItem> source);

    /**
     * Convert database entity PriceListItem to PriceListItemDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PriceListItemDTO mapApiDtoToDataApiDto (PriceListItem source);


}
