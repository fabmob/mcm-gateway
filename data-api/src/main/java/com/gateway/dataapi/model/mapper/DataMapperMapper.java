package com.gateway.dataapi.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.database.model.DataMapper;

@Mapper
public interface DataMapperMapper {
	@Mapping(target = "mspActionId", source = "action.mspActionId")
	DataMapperDTO mapEntityToDto(DataMapper source);

	List<DataMapperDTO> mapEntityToDto(List<DataMapper> source);

	List<DataMapper> mapDtoToEntity(List<DataMapperDTO> source);

	@Mapping(target = "action.mspActionId", source = "mspActionId")
	DataMapper mapDtoToEntity(DataMapperDTO source);
}
