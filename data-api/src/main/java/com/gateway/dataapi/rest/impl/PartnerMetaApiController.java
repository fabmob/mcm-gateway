package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.constants.DataApiPathDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.PartnerTypeRequestHeader;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.commonapi.utils.enums.TypeEnum;
import com.gateway.dataapi.service.PartnerMetaService;
import com.gateway.database.model.PartnerMeta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gateway.commonapi.constants.DataApiPathDict.*;

@RestController
@Validated
@RequestMapping(DataApiPathDict.PARTNER_METAS_BASE_PATH)
@Slf4j
public class PartnerMetaApiController {

    private static final String GET_ALL_PARTNER_METAS_BY_EXAMPLE = "************* Get All Partner metas with an example ************* ";

    @Autowired
    private PartnerMetaService partnerMetaService;


    /**
     * Return all PartnerMeta
     *
     * @return List of PartnerMeta
     */
    @Operation(summary = "Retrieve metadata for all managed partners.", description = "Route used to retrieve all metadata for all managed partners. Filters may be used on partner type (MSP or MaaS), type (PUBLIC_TRANSPORT, CARPOOLING, PARKING, EV_CHARGING, SELF_SERVICE_BICYCLE, CAR_SHARING, FREE_FLOATING, TAXI_VTC, MAAS_APPLICATION, MAAS_EDITOR), part of the name, and/or part of the operator.", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_METAS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerMetaDTO>> getPartnerMetas(@RequestHeader(required = false, name = "X-PARTNER-TYPE") PartnerTypeRequestHeader callerPartnerType,
                                                                @RequestParam(required = false) PartnerTypeEnum partnerType,
                                                                @RequestParam(required = false) TypeEnum type,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String operator) {
        log.info(GET_ALL_PARTNER_METAS_BY_EXAMPLE);
        PartnerMeta partnerMetaExample = new PartnerMeta();

        //Check caller from the header and hydrate the example to filter on it

        if (callerPartnerType == PartnerTypeRequestHeader.MSP) {
            partnerMetaExample.setPartnerType(PartnerTypeEnum.MAAS.value);
        } else if (callerPartnerType == PartnerTypeRequestHeader.MAAS) {
            partnerMetaExample.setPartnerType(PartnerTypeEnum.MSP.value);
        } else if (partnerType != null) {
            //We admit it's an admin that make the call
            partnerMetaExample.setPartnerType(partnerType.value);
        }

        //hydrate the example with filters
        if (type != null) {
            partnerMetaExample.setType(type.toString());
        }
        if (name != null) {
            partnerMetaExample.setName(name);
        }
        if (operator != null) {
            partnerMetaExample.setOperator(operator);
        }


        return ResponseEntity.ok(partnerMetaService.getPartnerMetasByExample(partnerMetaExample));
    }


    /**
     * Return a PartnerMeta which corresponds to idPartner passed as a parameter
     *
     * @param id Identifier of the partnerMeta
     * @return specified PartnerMeta by its id
     */
    @Operation(summary = "Retrieve metadata for a specified partner.", description = "Route used to retrieve all metadata for a specified partner.", tags = {PARTNER_METAS_TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerMetaDTO> getPartnerMetaFromId(@PathVariable("id") UUID id) {
        PartnerMetaDTO partnerMetaDTO = partnerMetaService.getPartnerMeta(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(GlobalConstants.OUTPUT_STANDARD, StandardEnum.OTHER.toString());
        return ResponseEntity.ok().headers(headers).body(partnerMetaDTO);
    }

    /**
     * Add a specified PartnerMeta
     *
     * @param body PartnerMeta to add
     * @return the PartnerMeta posted
     */
    @Operation(summary = "Create metadata for a new partner.", description = "Route used to create metadata for a new MSP or MaaS partner.", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = PARTNER_METAS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PartnerMetaDTO> postPartnerMeta(@RequestBody PartnerMetaDTO body) {
        PartnerMetaDTO partnerMetaDTO;
        partnerMetaDTO = partnerMetaService.postPartnerMeta(body);
        partnerMetaService.refreshCachePartnerMetas();
        return ResponseEntity.created(URI.create(CommonUtils.placeholderFormat(PARTNER_META_PATH, "id", partnerMetaDTO.getPartnerId().toString()))).body(partnerMetaDTO);
    }

    /***
     * Update PartnerMeta
     *
     * @param id   Identifier of the partnerMeta
     * @param body PartnerMeta to add
     * @return no content
     */
    @Operation(summary = "Update metadata for a specified partner.", description = "Route used to update metadata for a specified partner.", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Updated successfully No content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = PARTNER_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putPartnerMeta(@PathVariable("id") UUID id, @RequestBody PartnerMetaDTO body) {
        this.partnerMetaService.putPartnerMeta(id, body);
        partnerMetaService.refreshCachePartnerMetas();
        return ResponseEntity.noContent().build();
    }

    /**
     * delete a PartnerMeta
     *
     * @param id Identifier of the partnerMeta
     * @return no content
     */
    @Operation(summary = "Delete metadata for a specified partner.", description = "Route used to delete all metadata for a specified partner.", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = PARTNER_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deletePartnerMeta(@PathVariable("id") UUID id) {
        this.partnerMetaService.deletePartnerMeta(id);
        partnerMetaService.refreshCachePartnerMetas();
        return ResponseEntity.noContent().build();
    }

    /**
     * patch PartnerMeta
     *
     * @param id Identifier of the partnerMeta
     * @return the PartnerMeta patched
     */
    @Operation(summary = "Update specified metadata for a specified partner.", description = "Route used to update one or several specified metadata for a specified partner.", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PatchMapping(value = PARTNER_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchPartnerMeta(@PathVariable("id") UUID id, @RequestBody Map<String, Object> body) {
        // patch the object
        PartnerMetaDTO partnerMetaDTO = partnerMetaService.patchPartnerMeta(body, id);
        partnerMetaService.refreshCachePartnerMetas();
        return ResponseEntity.ok(partnerMetaDTO);
    }
}
