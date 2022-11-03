package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.database.model.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DataMapperMapper {
	@Mapping(target = "partnerActionId", source = "action.partnerActionId")
	DataMapperDTO mapEntityToDto(DataMapper source);

	List<DataMapperDTO> mapEntityToDto(List<DataMapper> source);

	List<DataMapper> mapDtoToEntity(List<DataMapperDTO> source);

	@Mapping(target = "action.partnerActionId", source = "partnerActionId")
	DataMapper mapDtoToEntity(DataMapperDTO source);
}
