package com.gateway.database.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "msp_standard", schema = "msp")
@AllArgsConstructor
@NoArgsConstructor
public class MspStandard implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StandardPK id;

    @Column(name = "msp_standard_id", unique = true, nullable = false)
    private UUID mspStandardId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "standard_name", length = 20)
    private String standardName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adapter_id")
    private Adapters adapter;

}
