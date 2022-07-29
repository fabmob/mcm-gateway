package com.gateway.dataapi.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.database.model.Token;

@Mapper
public interface TokenMapper {
	@Mapping(target = "mspMetaId", source = "msp.mspId")
	TokenDTO mapEntityToDto(Token source);

	List<TokenDTO> mapEntityToDto(List<Token> source);

	List<Token> mapDtoToEntity(List<TokenDTO> source);

	@Mapping(target = "msp.mspId", source = "mspMetaId")
	Token mapDtoToEntity(TokenDTO source);
}
