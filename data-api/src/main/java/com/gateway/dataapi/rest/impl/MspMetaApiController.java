package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.service.MspMetaService;
import com.gateway.dataapi.util.constant.DataApiPathDict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gateway.dataapi.util.constant.DataApiPathDict.*;

@RestController
@RequestMapping(DataApiPathDict.MSP_METAS_BASE_PATH)
public class MspMetaApiController {

    @Autowired
    private MspMetaService mspMetaService;

    /**
     * Return all mspMeta
     *
     * @return List of MspMeta
     */
    @Operation(summary = "Get the list of the Msp Metas", description = "Description Get the list of the Msp Metas", tags = {
            MSP_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_METAS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MspMetaDTO>> getMspMetas() {
        List<MspMetaDTO> mspMetas = mspMetaService.getMspMetas();
        return ResponseEntity.ok(mspMetas);
    }


    /**
     * Return a mspMeta which corresponds to idMsp passed as a parameter
     *
     * @param id Identifier of the mespMeta
     * @return specified mspMeta by its id
     */
    @Operation(summary = "Get the specified Msp Meta", description = "Description Get the specified Msp Meta", tags = {MSP_METAS_TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MspMetaDTO> getMspMetaFromId(@PathVariable("id") UUID id) {
        MspMetaDTO mspMetaDTO = mspMetaService.getMspMeta(id);
        return ResponseEntity.ok(mspMetaDTO);
    }

    /**
     * Add a specified mspMeta
     *
     * @param body mspMeta to add
     * @return the mspMeta posted
     */
    @Operation(summary = "Create specified Msp Meta", description = "Description Create the specified Msp Meta", tags = {
            MSP_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = MSP_METAS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MspMetaDTO> postMspMeta(@RequestBody MspMetaDTO body) {
        MspMetaDTO mspMetaDTO = mspMetaService.postMspMeta(body);
        return ResponseEntity.created(URI.create(CommonUtils.placeholderFormat(MSP_META_PATH, "id", mspMetaDTO.getMspId().toString()))).body(mspMetaDTO);
    }

    /***
     * Update MspMeta
     *
     * @param id   Identifier of the mespMeta
     * @param body mspMeta to add
     * @return no content
     */
    @Operation(summary = "Update the specified Msp Meta", description = "Description Update the specified Msp Meta", tags = {
            MSP_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Updated successfully No content", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = MSP_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putMspMeta(@PathVariable("id") UUID id, @RequestBody MspMetaDTO body) {
        this.mspMetaService.putMspMeta(id, body);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete a mspMeta
     *
     * @param id Identifier of the mespMeta
     * @return no content
     */
    @Operation(summary = "Delete the specified Msp Meta", description = "Description Delete the specified Msp Meta", tags = {
            MSP_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = MSP_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteMspMeta(@PathVariable("id") UUID id) {
        this.mspMetaService.deleteMspMeta(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * patch mspMeta
     *
     * @param id Identifier of the mespMeta
     * @return the mspMeta patched
     */
    @Operation(summary = "Patch the specified Msp Meta", description = "Description Patch the specified Msp Meta", tags = {
            MSP_METAS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PatchMapping(value = MSP_META_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchMspMeta(@PathVariable("id") UUID id, @RequestBody Map<String, Object> body) {
        // patch the object
        MspMetaDTO mspMetaDTO = mspMetaService.patchMspMeta(body, id);
        return ResponseEntity.ok(mspMetaDTO);
    }
}
