package com.gateway.adapter.rest;

import com.gateway.adapter.service.DefaultAdapterService;
import com.gateway.adapter.utils.constant.AdapterPathDict;
import com.gateway.commonapi.constants.ControllerMessageDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.ACTION_ID_PARAM;
import static com.gateway.adapter.utils.constant.AdapterMessageDict.PARTNER_ID_PARAM;
import static com.gateway.adapter.utils.constant.AdapterPathDict.GLOBAL_PATH;
import static com.gateway.commonapi.constants.ControllerMessageDict.*;

@Slf4j
@Validated
@RestController
@RequestMapping(GLOBAL_PATH)
public class DefaultAdapterController {

    @Autowired
    private DefaultAdapterService defaultAdapterService;

    @Operation(summary = "Retrieve an adapter for specified action and partner.", description = "Route used to retrieve an adapter for specified action and partner and, if specified, the standard used for output error and a valid code.", tags = {
            "adapter"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = AdapterPathDict.ADAPTER_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adaptGet(@RequestParam(name = ACTION_ID_PARAM) @NotNull UUID partnerActionId, @RequestParam(name = PARTNER_ID_PARAM) @NotNull UUID partnerId, @RequestParam(required = false) Map<String, String> params,
                                           @RequestHeader(name = GlobalConstants.OUTPUT_STANDARD, required = false, defaultValue = "") @Parameter(description = ControllerMessageDict.STANDARD_HEADER_DESCRIPTION,
                                                   example = "tomp-1.3.0", array = @ArraySchema(schema = @Schema(implementation = StandardEnum.class))) String outputStandard, @RequestHeader(name = GlobalConstants.VALID_CODES, required = false, defaultValue = "") String validCodes) throws IOException, IntrospectionException, JSONException {
        CallUtils.saveOutputStandardInCallThread(outputStandard);
        CallUtils.saveValidCodesInCallThread(validCodes);

        log.info("Call of service default GET adapter");
        log.debug(String.format("partnerActionId param found %s", params.get(ACTION_ID_PARAM)));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format("param [%s]=%s", key, value)));
        }
        return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, partnerActionId, partnerId, null), HttpStatus.OK);
    }


    @Operation(summary = "Create an adapter for specified action and partner.", description = "Route used to create an adapter for specified action and partner and, if specified, the standard used for output error and a valid code. The adapter converts a MaaS request regarding datamapping into the partner format.", tags = {
            "adapter"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = AdapterPathDict.ADAPTER_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> adaptPost(@RequestParam(name = ACTION_ID_PARAM) @NotNull UUID partnerActionId,
                                            @RequestParam(name = PARTNER_ID_PARAM) @NotNull UUID partnerId,
                                            @RequestParam(required = false) Map<String, String> params,
                                            @RequestBody Optional<Map<String, Object>> body,
                                            @RequestHeader(name = GlobalConstants.OUTPUT_STANDARD, required = false, defaultValue = "") @Parameter(description = ControllerMessageDict.STANDARD_HEADER_DESCRIPTION,
                                                    example = "tomp-1.3.0", array = @ArraySchema(schema = @Schema(implementation = StandardEnum.class))) String outputStandard, @RequestHeader(name = GlobalConstants.VALID_CODES, required = false, defaultValue = "") String validCodes) throws IOException, IntrospectionException, JSONException {
        CallUtils.saveOutputStandardInCallThread(outputStandard);
        CallUtils.saveValidCodesInCallThread(validCodes);

        log.info("Call of service default POST adapter");
        log.debug(String.format("partnerActionId param found %s", params.get(ACTION_ID_PARAM)));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format("param [%s]=%s", key, value)));
        }

        if (body.isPresent()) {
            return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, partnerActionId, partnerId, body.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, partnerActionId, partnerId, null), HttpStatus.OK);
        }
    }
}
