package com.gateway.database.service;

import java.util.UUID;

import com.gateway.database.model.Token;

public interface TokenDatabaseService {
    /**
     * Add a new Token
     *
     * @param token Token object
     * @return Token informations for the Token added
     */
    Token addToken(Token token);

    /**
     * Delete a Token
     *
     * @param id Identifier of the Token
     */
    void deleteToken(UUID id);

    /**
     * Retrieve a Token informations.
     *
     * @param id Identifier of the Token
     * @return Token informations for the Token
     */
    Token findTokenById(UUID id);

    /**
     * Get Token from MspMeta id
     *
     * @param id Identifier of the MspMetaDto
     * @return Token
     */
    Token findByMspMetaId(UUID id);

}
