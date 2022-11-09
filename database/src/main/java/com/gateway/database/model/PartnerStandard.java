package com.gateway.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "partner_standard", schema = "msp")
@AllArgsConstructor
@NoArgsConstructor
public class PartnerStandard implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StandardPK id;

    @Column(name = "partner_standard_id", unique = true, nullable = false)
    private UUID partnerStandardId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "standard_name", length = 20)
    private String standardName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adapter_id")
    private Adapters adapter;

}
