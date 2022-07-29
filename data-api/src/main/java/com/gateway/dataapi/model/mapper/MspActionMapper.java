package com.gateway.dataapi.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.database.model.MSPActions;


@Mapper
public interface MspActionMapper {

	MspActionDTO mapEntityToDto(MSPActions source);

	List<MspActionDTO> mapEntityToDto(List<MSPActions> source);

	List<MSPActions> mapDtoToEntity(List<MspActionDTO> source);

	MSPActions mapDtoToEntity(MspActionDTO source);

}
