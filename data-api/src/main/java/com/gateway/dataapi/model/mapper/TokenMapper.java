package com.gateway.dataapi.model.mapper;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.database.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TokenMapper {
	@Mapping(target = "partnerId", source = "partner.partnerId")
	TokenDTO mapEntityToDto(Token source);

	List<TokenDTO> mapEntityToDto(List<Token> source);

	List<Token> mapDtoToEntity(List<TokenDTO> source);

	@Mapping(target = "partner.partnerId", source = "partnerId")
	Token mapDtoToEntity(TokenDTO source);
}
