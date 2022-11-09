package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PartnerCallsDTO;
import com.gateway.database.model.PartnerCalls;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PartnerCallsMapper {
	
	@Mapping(target = "partnerActionId", source = "action.partnerActionId")
	PartnerCallsDTO mapEntityToDto(PartnerCalls source);

	List<PartnerCallsDTO> mapEntityToDto(List<PartnerCalls> source);

	List<PartnerCalls> mapDtoToEntity(List<PartnerCallsDTO> source);

	@Mapping(target = "action.partnerActionId", source = "partnerActionId")
	PartnerCalls mapDtoToEntity(PartnerCallsDTO source);

}
