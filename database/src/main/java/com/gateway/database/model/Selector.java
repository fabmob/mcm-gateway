package com.gateway.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "selector", schema = "msp")
public class Selector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "selector_id", unique = true, nullable = false)
    private UUID selectorId;

    @Column(name = "key", length = 30)
    private String key;

    @Column(name = "value", length = 50)
    private String value;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "selector")
    private MSPActions mspActions;
}