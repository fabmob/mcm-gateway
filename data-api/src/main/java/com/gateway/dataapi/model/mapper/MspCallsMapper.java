package com.gateway.dataapi.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gateway.commonapi.dto.data.MspCallsDTO;
import com.gateway.database.model.MSPCalls;

@Mapper
public interface MspCallsMapper {
	
	@Mapping(target = "mspActionId", source = "action.mspActionId")
	MspCallsDTO mapEntityToDto(MSPCalls source);

	List<MspCallsDTO> mapEntityToDto(List<MSPCalls> source);

	List<MSPCalls> mapDtoToEntity(List<MspCallsDTO> source);

	@Mapping(target = "action.mspActionId", source = "mspActionId")
	MSPCalls mapDtoToEntity(MspCallsDTO source);

}
