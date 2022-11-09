package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.constants.DataApiPathDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.dataapi.service.PartnerMetaService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gateway.commonapi.constants.DataApiPathDict.*;

@RestController
@RequestMapping(DataApiPathDict.PARTNER_METAS_BASE_PATH)
@Slf4j
public class PartnerMetaApiController {

    private static final String GET_ALL_PARTNER_METAS_OR_GET_BY_PARTNER_TYPE = "************* Get All Partner metas Or Get By partner type ************* ";

    @Autowired
    private PartnerMetaService partnerMetaService;


    /**
     * Return all PartnerMeta
     *
     * @return List of PartnerMeta
     */
    @Operation(summary = "Get the list of the Partner Metas", description = "Description Get the list of the Partner Metas", tags = {
            PARTNER_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_METAS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerMetaDTO>> getPartnerMetas(@RequestParam(required = false) PartnerTypeEnum partnerType) {
        log.info(GET_ALL_PARTNER_METAS_OR_GET_BY_PARTNER_TYPE);
        if (partnerType != null) {
            List<PartnerMetaDTO> partnerMetas = partnerMetaService.getPartnerMetasByPartnerType(partnerType);
            return ResponseEntity.ok(partnerMetas);
        }
        List<PartnerMetaDTO> partnerMetas = partnerMetaService.getPartnerMetas();
        return ResponseEntity.ok(partnerMetas);
    }


    /**
     * Return a PartnerMeta which corresponds to idPartner passed as a parameter
     *
     * @param id Identifier of the partnerMeta
     * @return specified PartnerMeta by its id
     */
    @Operation(summary = "Get the specified Partner Meta", description = "Description Get the specified Partner Meta", tags = {PARTNER_METAS_TAG})
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
    @Operation(summary = "Create specified Partner Meta", description = "Description Create the specified Partner Meta", tags = {
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
    @Operation(summary = "Update the specified Partner Meta", description = "Description Update the specified Partner Meta", tags = {
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
    @Operation(summary = "Delete the specified Partner Meta", description = "Description Delete the specified Partner Meta", tags = {
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
    @Operation(summary = "Patch the specified Partner Meta", description = "Description Patch the specified Partner Meta", tags = {
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
