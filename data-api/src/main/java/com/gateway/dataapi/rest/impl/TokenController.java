package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.DataApiPathDict.*;


@RestController
@RequestMapping(TOKENS_BASE_PATH)
@Slf4j
public class TokenController {

    private static final String ADD_TOKEN = "************* Add Token ************* ";
    private static final String DELETE_TOKEN = "************* Delete Token ************* ";
    private static final String GET_ALL_TOKEN_OR_BY_PARTNER_META_ID = "************* Get All Tokens with optional filter partnerMetaId ************* ";
    TokenService tokenService;

    public TokenController(TokenService tokenService) {
        super();
        this.tokenService = tokenService;
    }

    /**
     * Add a token
     *
     * @param token to add
     * @return the token posted
     */
    @Operation(summary = "Create a token for a specified partner and a specified access token.", description = "Route used to create a token for a specified partner and a specified access token with an expiration date.", tags = {
            TOKENS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = TOKENS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TokenDTO> addToken(@RequestBody TokenDTO token) {
        log.info(ADD_TOKEN);
        TokenDTO newToken = tokenService.addToken(token);
        return new ResponseEntity<>(newToken, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve data for a specified token.", description = "Route used to retrieve data for a specified token.", tags = {
            TOKENS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = TOKEN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> geTokenFromId(@PathVariable("id") UUID id) {
        log.info("************* Get Token By Id ************* ");
        TokenDTO token = tokenService.getTokenFromId(id);
        return ResponseEntity.ok(token);

    }

    @Operation(summary = "Delete a specified token.", description = "Route used to delete a specified token.", tags = {TOKENS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = TOKEN_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteToken(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_TOKEN);
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve token data for a specified partner.", description = "Route used to retrieve token data for a specified partner.", tags = {TOKENS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = TOKENS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> getAllTokens(@RequestParam(name = "partnerMetaId", required = true) UUID partnerMetaId) {
        log.info(GET_ALL_TOKEN_OR_BY_PARTNER_META_ID);
        TokenDTO token = tokenService.getByPartnerMetaId(partnerMetaId);
        return new ResponseEntity<>(token, HttpStatus.OK);

    }

}
