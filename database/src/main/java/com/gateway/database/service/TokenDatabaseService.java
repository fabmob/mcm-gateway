package com.gateway.database.service;

import com.gateway.database.model.Token;

import java.util.UUID;

public interface TokenDatabaseService {
    /**
     * Add a new Token
     *
     * @param token Token object
     * @return Token information for the Token added
     */
    Token addToken(Token token);

    /**
     * Delete a Token
     *
     * @param id Identifier of the Token
     */
    void deleteToken(UUID id);

    /**
     * Retrieve Token information.
     *
     * @param id Identifier of the Token
     * @return Token information for the Token
     */
    Token findTokenById(UUID id);

    /**
     * Get Token from MspMeta id
     *
     * @param id Identifier of the MspMetaDto
     * @return Token
     */
    Token findByPartnerMetaId(UUID id);

}
