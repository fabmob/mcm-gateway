package com.gateway.requestrelay.rest;


import com.gateway.commonapi.constants.ControllerMessageDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.dto.requestrelay.PartnerCallsFinalDTO;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.requestrelay.service.RequestRelayService;
import com.gateway.requestrelay.utils.enums.Protocol;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;

import static com.gateway.requestrelay.utils.constant.RequestRelayPathDict.REQUEST_RELAY_FULL_PATH;
import static com.gateway.requestrelay.utils.constant.RequestRelayPathDict.REQUEST_RELAY_PATH;
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


    /**
     * Execute a specified HTTP Call.
     *
     * @param callInfos : Object {@link PartnerCallsFinalDTO} containing the url, method (GET,POST...), body and headers for the call
     * @return {@link ResponseEntity} contains HTTP response
     */


    @Operation(summary = "Create a request relay for a specified protocol.", description = "Route used to create a request relay for a specified protocol and, if specified, the standard used for output error and a valid code. The request relay executes the request received from the adaptor process.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class))),
            @ApiResponse(responseCode = "503", description = "Unavailable service", content = @Content(schema = @Schema(implementation = ServiceUnavailable.class)))})
    @PostMapping(value = "",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> execute(@Valid @RequestBody PartnerCallsFinalDTO callInfos, @RequestParam Protocol protocol,
                                          @RequestHeader(name = GlobalConstants.OUTPUT_STANDARD, required = false, defaultValue = "") @Parameter(description = ControllerMessageDict.STANDARD_HEADER_DESCRIPTION,
                                                  example = "tomp-1.3.0", array = @ArraySchema(schema = @Schema(implementation = StandardEnum.class))) String outputStandard, @RequestHeader(name = GlobalConstants.VALID_CODES, required = false, defaultValue = "") String validCodes) {


        //TODO: Adapters
        //  Adapters should send the PartnerCallsFinalDTO callInfos object constructed during datamapping.
        //  It contains: url with all the params = assignParameters(...) of CustomParamServiceImpl of Maax, method, final body, final headers including token = processSecurityHeaders(...) of CustomParamServiceImpl of Maax

        boolean preserveOriginalErrors = false;
        if (StringUtils.isNotBlank(outputStandard)) {
            preserveOriginalErrors = CommonUtils.shouldPreserveResponseStatus(outputStandard);
        }

        if (protocol.equals(SOAP)) {
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), REQUEST_RELAY_FULL_PATH + SOAP));
        } else {
            return requestRelayService.processCalls(callInfos, preserveOriginalErrors);

        }
    }
}
