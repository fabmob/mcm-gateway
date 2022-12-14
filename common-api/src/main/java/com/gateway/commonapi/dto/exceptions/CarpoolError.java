/*
 * Transport Operator MaaS Provider API
 * An API between MaaS providers and transport operators for booking trips and corresponding assets. <p>The documentation (examples, process flows and sequence diagrams) can be found at <a href=\"https://github.com/TOMP-WG/TOMP-API/\">github</a>.
 *
 * OpenAPI spec version: 1.3.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.gateway.commonapi.dto.exceptions;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Schema(description = "An error that the service may send, e.g. in case of invalid input, missing authorization or internal service error.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-11T15:04:35.720Z[GMT]")
public class CarpoolError {
    @SerializedName("error")
    private String error = null;

    /**
     * The category of this type of error.
     *
     * @return type
     **/
    @Schema(description = "Description of the error.")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public CarpoolError() {
    }

    public CarpoolError(String detail) {
        this.error = detail;
    }


}
