package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.database.model.Adapters;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AdaptersMapper {
    AdaptersDTO mapEntityToDto(Adapters source);

    List<AdaptersDTO> mapEntityToDto(List<Adapters> source);

    List<Adapters> mapDtoToEntity(List<AdaptersDTO> source);

    Adapters mapDtoToEntity(AdaptersDTO source);
}
