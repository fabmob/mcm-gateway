package com.gateway.api.model.mapper;

import com.gateway.api.model.PriceList;
import com.gateway.commonapi.dto.data.PriceListDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Class using mapstruct to Map DTO from datab-api and DTO from api.
 */

@Mapper
public interface PriceListDTOMapper {
    /**
     * Convert data-api  PriceListDTO to api PriceList
     * @param source entity object to map
     * @return dto result of the mapping
     */
    PriceList mapDataApiDtoToApiDto(PriceListDTO source);

    /**
     * Convert database entity List PriceList to List PriceListDTO
     * @param source list of entity object to map
     * @return list dto result of the mapping
     */
    List<PriceList> mapDataApiDtoToApiDto(List<PriceListDTO> source);

    /**
     * Convert List PriceListDTO to database List PriceList entity
     * @param source list of dto object to map
     * @return list entity result of the mapping
     */
    List<PriceListDTO>mapApiDtoToDataApiDto(List<PriceList> source);

    /**
     * Convert database entity PriceList to PriceListDTO
     * @param source dto object to map
     * @return enity result of the mapping
     */
    PriceListDTO mapApiDtoToDataApiDto(PriceList source);


}
