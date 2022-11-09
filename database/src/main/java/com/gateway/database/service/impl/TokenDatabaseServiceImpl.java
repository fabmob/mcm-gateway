package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.Token;
import com.gateway.database.repository.TokensRepository;
import com.gateway.database.service.TokenDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Service
public class TokenDatabaseServiceImpl implements TokenDatabaseService {

    @Autowired
    private TokensRepository tokenRepository;

    @Autowired
    private ErrorMessages errorMessage;

    public TokenDatabaseServiceImpl(TokensRepository tokenRepository) {
        super();

        this.tokenRepository = tokenRepository;
    }

    public TokensRepository getTokenRepository() {
        return tokenRepository;
    }

    public TokenDatabaseServiceImpl() {
    }

    /**
     * Add a new Token
     *
     * @param token Token object
     * @return Token information for the Token added
     */
    @Override
    public Token addToken(Token token) {
        try {
            return tokenRepository.save(token);
        } catch (Exception ex) {
            throw new NotFoundException(CommonUtils.placeholderFormat(TOKEN_WITH_PARTNER_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, token.getPartner().getPartnerId().toString()));
        }
    }

    /**
     * Delete a Token
     *
     * @param id Identifier of the Token
     */
    @Override
    public void deleteToken(UUID id) {
        try {
            tokenRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(TOKEN_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()));
        }
    }

    /**
     * Retrieve a Token information.
     *
     * @param id Identifier of the Token
     * @return Token information for the Token
     */
    @Override
    public Token findTokenById(UUID id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(TOKEN_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));
    }

    /**
     * Get Token from PartnerMeta id
     *
     * @param id Identifier of the PartnerMetaDto
     * @return Token
     */
    @Override
    public Token findByPartnerMetaId(UUID id) {
        return tokenRepository.findByPartnerPartnerId(id);
    }
}
