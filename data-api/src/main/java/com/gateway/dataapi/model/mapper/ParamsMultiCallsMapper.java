package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.ParamsMultiCallsDTO;
import com.gateway.database.model.ParamsMultiCalls;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ParamsMultiCallsMapper {

	ParamsMultiCallsDTO mapEntityToDto(ParamsMultiCalls source);

	List<ParamsMultiCallsDTO> mapEntityToDto(List<ParamsMultiCalls> source);

	List<ParamsMultiCalls> mapDtoToEntity(List<ParamsMultiCallsDTO> source);

	Set<ParamsMultiCallsDTO> mapEntityToDto(Set<ParamsMultiCalls> source);

	Set<ParamsMultiCalls> mapDtoToEntity(Set<ParamsMultiCallsDTO> source);

	ParamsMultiCalls mapDtoToEntity(ParamsMultiCallsDTO source);
}
