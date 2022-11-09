package com.gateway.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GATEWAY_PARAMS", schema = "configuration")
public class GatewayParams implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "param_key", unique = true, nullable = false, length = 50)
    private String paramKey;

    @Column(name = "param_value", length = 255)
    private String paramValue;


}
