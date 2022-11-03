package com.gateway.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "data_mapper", schema = "msp")
public class DataMapper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "data_mapper_id", unique = true, nullable = false)
    private UUID dataMapperId;

    @Column(name = "internal_field", length = 150)
    private String internalField;

    @Column(name = "external_field", length = 150)
    private String externalField;

    @Column(name = "is_array")
    private Integer isArray;

    @Column(name = "format")
    private String format;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "contained_value", length = 150)
    private String containedValue;

    @Column(name = "default_value", length = 200)
    private String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private PartnerActions action;


}
