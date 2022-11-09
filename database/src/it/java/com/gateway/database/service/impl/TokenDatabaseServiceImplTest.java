package com.gateway.database.service.impl;


import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.Token;
import com.gateway.database.repository.PartnerMetaRepository;
import com.gateway.database.repository.TokensRepository;
import com.gateway.database.service.TokenDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class TokenDatabaseServiceImplTest {

    @TestConfiguration
    public static class TokenDatabaseServiceImplTestContextConfiguration {
        @Bean
        public TokenDatabaseService tokenDatabaseService() {
            return new TokenDatabaseServiceImpl();
        }
    }

    @MockBean
    private TokensRepository tokenRepository;

    @MockBean
    private ErrorMessages errorMessage;


    @MockBean
    private PartnerMetaRepository partnerRepository;

    @Autowired
    private TokenDatabaseService tokenDatabaseService;

    @Test
    public void testAddToken() {
        Token token = new Token();
        Mockito.when(tokenRepository.save(token)).thenReturn(token);
        Token tokenToAdd = tokenDatabaseService.addToken(token);
        assertEquals(token.getAccessToken(), tokenToAdd.getAccessToken());
    }


    @Test
    public void testDeleteToken() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        tokenDatabaseService.deleteToken(id);
        verify(tokenRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindTokenById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Token token = new Token();
        token.setTokenId(id);
        Mockito.when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        Token tokenToFind = tokenDatabaseService.findTokenById(id);
        assertEquals(token, tokenToFind);
    }


    @Test
    public void testConstructorTokenDatabaseServiceImpl() {
        TokenDatabaseServiceImpl tokenDatabaseServiceImpl =
                new TokenDatabaseServiceImpl(tokenRepository);
        assertEquals(tokenRepository, tokenDatabaseServiceImpl.getTokenRepository());

    }
}