package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * User's identifier
     */
    @Schema(name = "id",
            required = true,
            example = "user987654",
            description = "User's identifier. It MUST be unique for a given `operator`.")
    @JsonProperty("id")
    private String id = null;

    /**
     * The operator identifier.
     *
     * @return operator
     **/
    @Schema(name = "operator",
            required = true,
            example = "'carpool.mycity.com",
            description = "The operator identifier. MUST be a Fully Qualified Domain Name (example carpool.mycity.com) owned by the operator or a Partially Qualified Domain Name (example operator.org) owned and exclusively operated by the operator. Operators SHOULD always send the same value.")
    @JsonProperty("operator")
    private String operator = null;

    /**
     * User's alias.
     **/
    @Schema(name = "alias",
            required = true,
            example = "JeanD",
            description = "User's alias.")
    @JsonProperty("alias")
    private String alias = null;

    /**
     * User's first name.
     **/
    @Schema(name = "firstName",
            description = "User's first name.",
            example = "Jean"
    )
    @JsonProperty("firstName")
    private String firstName = null;

    /**
     * User's last name.
     **/
    @Schema(name = "lastName",
            description = "User's last name.",
            example = "Dupond"
    )
    @JsonProperty("lastName")
    private String lastName = null;

    /**
     * User's grade from 1 to 5.
     * minimum: 1
     * maximum: 5
     *
     * @return grade
     **/
    @Schema(name = "grade",
            description = "User's grade from 1 to 5.",
            example = "4")
    @JsonProperty("grade")
    @Min(1)
    @Max(5)
    private Integer grade = null;

    /**
     * User's profile picture absolute URL.
     **/
    @Schema(name = "picture",
            description = "User's profile picture absolute URL.",
            example = "carpool.mycity.com/user/user987654/picture.jpg")
    @JsonProperty("picture")
    private String picture = null;

    /**
     * User's gender. 'O' stands for 'Other'.
     **/
    @Schema(name = "gender",
            description = "User's gender. 'O' stands for 'Other'.")
    @JsonProperty("gender")
    private GenderEnum gender = null;

    /**
     * true if the identity of this user has been verified by the operator or a third party; and the firstName, lastName, birthdate have been confirmed as identitical to an official identity proof document. Can be left empty if the information is not available.
     **/
    @Schema(name = "verifiedIdentity",
            description = "true if the identity of this user has been verified by the operator or a third party; and the firstName, lastName, birthdate have been confirmed as identitical to an official identity proof document. Can be left empty if the information is not available.",
            example = "true")
    @JsonProperty("verifiedIdentity")
    private Boolean verifiedIdentity = null;
}
