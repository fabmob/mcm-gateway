package com.gateway.requestrelay.rest;


import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.dto.requestrelay.MspCallsFinalDTO;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.requestrelay.service.RequestRelayService;
import com.gateway.requestrelay.utils.enums.Protocol;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;

import static com.gateway.requestrelay.utils.constant.RequestRelayPathDict.*;
import static com.gateway.requestrelay.utils.enums.Protocol.SOAP;


@Slf4j
@Validated
@RestController
@RequestMapping(REQUEST_RELAY_PATH)
public class RequestRelayController {

    @Autowired
    private RequestRelayService requestRelayService;

    @Autowired
    private ErrorMessages errorMessages;

    private static final String SOAP_MSG = "SOAP case not implemented yet";



    /**
     * Execute a specified HTTP Call.
     *
     *  @param callInfos : Object {@link MspCallsFinalDTO} containing the url, method (GET,POST...), body and headers for the call
     * 	@return {@link ResponseEntity} contains HTTP response
     *
     */


    @Operation(summary = "Relay requests to MSPs", description = "Execute request received from adaptator process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class))),
            @ApiResponse(responseCode = "503", description = "Unavailable service", content = @Content(schema = @Schema(implementation = ServiceUnavailable.class)))})
    @PostMapping(value = "",
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE },
            consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<String> execute(@Valid @RequestBody MspCallsFinalDTO callInfos, @RequestParam Protocol protocol) throws UnavailableException  {


        //TODO: Adapters
        //  Adapters should send the MspCallsFinalDTO callInfos object constructed during datamapping.
        //  It contains: url with all the params = assignParameters(...) of CustomParamServiceImpl of Maax, method, final body, final headers including token = processSecurityHeaders(...) of CustomParamServiceImpl of Maax


        if(protocol.equals(SOAP)){
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),REQUEST_RELAY_FULL_PATH+SOAP));
        } else {
            String mspResponse = requestRelayService.processCalls(callInfos);
            return new ResponseEntity<>(mspResponse,HttpStatus.OK);
        }


    }
}
