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

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * An error that the service may send, e.g. in case of invalid input, missing authorization or internal service error. See https://github.com/TOMP-WG/TOMP-API/wiki/TompError-handling-in-TOMP for further explanation of error code.
 */
@Schema(description = "An error that the service may send, e.g. in case of invalid input, missing authorization or internal service error. See https://github.com/TOMP-WG/TOMP-API/wiki/TompError-handling-in-TOMP for further explanation of error code.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-11T15:04:35.720Z[GMT]")
public class TompError {
    @SerializedName("errorcode")
    private Integer errorcode = null;

    @SerializedName("type")
    private String type = null;

    @SerializedName("title")
    private String title = null;

    @SerializedName("status")
    private Integer status = null;

    @SerializedName("detail")
    private String detail = null;

    @SerializedName("instance")
    private String instance = null;

    @SerializedName("timestamp")
    private String timestamp = null;


    public TompError() {
    }
    
    public TompError(Integer errorcode, String type, String title, String detail) {
        this.setStatus(HttpStatus.BAD_REQUEST.value());
        this.setErrorcode(errorcode);
        this.setType(type);
        this.setTitle(title);
        this.setDetail(detail);
        this.timestamp = new SimpleDateFormat(GlobalConstants.GATEWAY_DATE_FORMAT).format(new Date());
        this.instance = "Gateway callId " + new ThreadLocalUserSession().get().getContextId();
    }

    public TompError errorcode(Integer errorcode) {
        this.errorcode = errorcode;
        return this;
    }

    /**
     * The TOMP specific error code. See https://github.com/TOMP-WG/TOMP-API/wiki/TompError-handling-in-TOMP for more details of this error.
     *
     * @return errorcode
     **/
    @Schema(description = "The TOMP specific error code. See https://github.com/TOMP-WG/TOMP-API/wiki/TompError-handling-in-TOMP for more details of this error.")
    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public TompError type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The category of this type of error.
     *
     * @return type
     **/
    @Schema(description = "The category of this type of error.")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TompError title(String title) {
        this.title = title;
        return this;
    }

    /**
     * A short, human-readable summary of the problem type.  It SHOULD NOT change from occurrence to occurrence of the problem, except to match Content-Language
     *
     * @return title
     **/
    @Schema(description = "A short, human-readable summary of the problem type.  It SHOULD NOT change from occurrence to occurrence of the problem, except to match Content-Language")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TompError status(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * The HTTP status code ([RFC7231], Section 6) generated by the origin server for this occurrence of the problem.
     *
     * @return status
     **/
    @Schema(description = "The HTTP status code ([RFC7231], Section 6) generated by the origin server for this occurrence of the problem.")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public TompError detail(String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * A human-readable explanation specific to this occurrence of the problem, could match Content-Language
     *
     * @return detail
     **/
    @Schema(description = "A human-readable explanation specific to this occurrence of the problem, could match Content-Language")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public TompError instance(String instance) {
        this.instance = instance;
        return this;
    }

    /**
     * A URI reference that identifies the specific occurrence of the problem.  It may or may not yield further information if dereferenced.
     *
     * @return instance
     **/
    @Schema(description = "A URI reference that identifies the specific occurrence of the problem.  It may or may not yield further information if dereferenced.")
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }


    public TompError timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Schema(description = "Date of the error on ISO8601 format " + GlobalConstants.GATEWAY_DATE_FORMAT)
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TompError error = (TompError) o;
        return Objects.equals(this.errorcode, error.errorcode) &&
                Objects.equals(this.type, error.type) &&
                Objects.equals(this.title, error.title) &&
                Objects.equals(this.status, error.status) &&
                Objects.equals(this.detail, error.detail) &&
                Objects.equals(this.instance, error.instance) &&
                Objects.equals(this.timestamp, error.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorcode, type, title, status, detail, instance, timestamp);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TompError {\n");

        sb.append("    errorcode: ").append(toIndentedString(errorcode)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
        sb.append("    instance: ").append(toIndentedString(instance)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
