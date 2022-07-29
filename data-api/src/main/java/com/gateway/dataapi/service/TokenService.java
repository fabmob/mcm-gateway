package com.gateway.dataapi.service;

import java.util.UUID;

import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.exception.GlobalExceptionHandler;
import com.gateway.commonapi.exception.NotFoundException;


public interface TokenService {

	/**
	 * Add a new TokenDto
	 *
	 * @param token TokenDTO object
	 * @return TokenDTO informations for the TokenDTO added
	 */
	public TokenDTO addToken(TokenDTO token) throws GlobalExceptionHandler;


	/**
	 * Delete a TokenDTO
	 *
	 * @param id Identifier of the TokenDTO
	 */

	public void deleteToken(UUID id);

	/**
	 * Retrieve a TokenDto informations.
	 *
	 * @param id Identifier of the TokenDto
	 * @return TokenDto informations for the TokenDto
	 * @throws NotFoundException not found object
	 */
	public TokenDTO getTokenFromId(UUID id) throws NotFoundException;

	/**
	 * Get TokenDto from MspMeta id
	 *
	 * @param id Identifier of the MspMetaDto
	 * @return TokenDto
	 * @throws NotFoundException
	 */
	public TokenDTO getByMspMetaId(UUID id) throws NotFoundException;

}
