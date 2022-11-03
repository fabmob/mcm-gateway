package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.exception.GlobalExceptionHandler;
import com.gateway.commonapi.exception.NotFoundException;

import java.util.UUID;


public interface TokenService {

    /**
     * Add a new TokenDto
     *
     * @param token TokenDTO object
     * @return TokenDTO information for the TokenDTO added
     */
    TokenDTO addToken(TokenDTO token) throws GlobalExceptionHandler;


    /**
     * Delete a TokenDTO
     *
     * @param id Identifier of the TokenDTO
     */

    void deleteToken(UUID id);

    /**
     * Retrieve a TokenDto information.
     *
     * @param id Identifier of the TokenDto
     * @return TokenDto information for the TokenDto
     * @throws NotFoundException not found object
     */
    TokenDTO getTokenFromId(UUID id) throws NotFoundException;

    /**
     * Get TokenDto from PartnerMeta id
     *
     * @param id Identifier of the PartnerMetaDto
     * @return TokenDto
     * @throws NotFoundException
     */
    TokenDTO getByPartnerMetaId(UUID id) throws NotFoundException;

}
