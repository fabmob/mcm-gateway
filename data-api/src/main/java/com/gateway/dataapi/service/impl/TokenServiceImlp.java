package com.gateway.dataapi.service.impl;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.TokenMapper;
import com.gateway.dataapi.service.TokenService;
import com.gateway.database.model.Token;
import com.gateway.database.service.TokenDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.TOKEN_WITH_PARTNER_META_ID_IS_NOT_FOUND;

@Service
@Slf4j
public class TokenServiceImlp implements TokenService {

    @Autowired
    private TokenDatabaseService serviceToken;

    private final TokenMapper mapper = Mappers.getMapper(TokenMapper.class);


    /**
     * Add a new TokenDto
     *
     * @param tokenDTO TokenDTO object
     * @return TokenDTO information for the TokenDTO added
     */

    @Override
    public TokenDTO addToken(TokenDTO tokenDTO) {
        Token token = serviceToken.findByPartnerMetaId(tokenDTO.getPartnerId());
        if (token != null) {
            serviceToken.deleteToken(token.getTokenId());
        }
        token = serviceToken.addToken(mapper.mapDtoToEntity(tokenDTO));
        return mapper.mapEntityToDto(token);
    }

    /**
     * Delete a TokenDTO
     *
     * @param id Identifier of the TokenDTO
     */
    @Override
    public void deleteToken(UUID id) {
        serviceToken.deleteToken(id);
    }

    /**
     * Retrieve a TokenDto information.
     *
     * @param id Identifier of the TokenDto
     * @return TokenDto information for the TokenDto
     * @throws NotFoundException not found object
     */

    @Override
    public TokenDTO getTokenFromId(UUID id) throws NotFoundException {
        Token token = serviceToken.findTokenById(id);
        return mapper.mapEntityToDto(token);
    }

    /**
     * Get TokenDto from PartnerMeta id
     *
     * @param id Identifier of the PartnerMetaDto
     * @return TokenDto
     */

    @Override
    public TokenDTO getByPartnerMetaId(UUID id) {
        Token token;
        try {
            token = serviceToken.findByPartnerMetaId(id);
        } catch (Exception e) {
            log.error("TOKEN_WITH_PARTNER_META_ID_IS_NOT_FOUND");
            throw new NotFoundException(String.format(TOKEN_WITH_PARTNER_META_ID_IS_NOT_FOUND, id));
        }
        return mapper.mapEntityToDto(token);
    }

}
