package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.database.model.PartnerActions;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface PartnerActionMapper {

	PartnerActionDTO mapEntityToDto(PartnerActions source);

	List<PartnerActionDTO> mapEntityToDto(List<PartnerActions> source);

	List<PartnerActions> mapDtoToEntity(List<PartnerActionDTO> source);

	PartnerActions mapDtoToEntity(PartnerActionDTO source);

}
