package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * Bean for details about a potential action.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Details about a potential action")
@JsonPropertyOrder({"type", "deeplink", "apiCall", "webview", "url"})
public class Action implements Serializable {


    /**
     * Action.
     */
    @Schema(
            name = "type",
            description = "Type of action that can be performed",
            example = "BOOK_VEHICLE",
            required = true)
    @JsonProperty("type")
    @NotNull
    private ActionType type;

    /**
     * Deep link.
     */
    @Schema(
            name = "deeplink",
            description = "Deeplink for performing the action",
            example = "https://example.com/deeplink/action")
    @JsonProperty("deeplink")
    private String deeplink;

    /**
     * API Call.
     */
    @Schema(
            name = "apiCall",
            description = "Name of the API call for performing the action",
            example = "https://example.com/api/action")
    @JsonProperty("apiCall")
    private String apiCall;

    /**
     * Web view.
     */
    @Schema(
            name = "webview",
            description = "URL to display in a web view for performing the action",
            example = "https://example.com/api/action/book_vehicle")
    @JsonProperty("webview")
    private String webView;

    /**
     * URL.
     */
    @Schema(
            name = "url",
            description = "URL to display in a browser for performing the action",
            example = "https://example.com/api/action/book_vehicle")
    @JsonProperty("url")
    private String url;
}
