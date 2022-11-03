package com.gateway.database.model;

import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "partnerMeta")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "price_list", schema = "msp")
@FilterDef(name = "filtre", parameters = @ParamDef(name = "listType", type = "string"))
public class PriceList implements java.io.Serializable {
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(name = "property", value = "mspMeta"))
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_list_id")
    private UUID priceListId;

    /**
     * Comment on the price list.
     */
    @Column(name = "comment", length = 255)
    private String comment;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "priceList")
    @PrimaryKeyJoinColumn
    private PartnerMeta partnerMeta;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "priceListForDuration", cascade = CascadeType.PERSIST)
    @Filter(name = "filtre", condition = ":listType = 'duration'")
    private List<Duration> duration;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "priceListForDistance", cascade = CascadeType.PERSIST)
    @Filter(name = "filtre", condition = ":listType = 'distance'")
    private List<Distance> distance = new ArrayList<>();

    private UUID fkMspMeta;


    public PartnerMeta getPartnerMeta() {
        return this.partnerMeta;
    }

    public void setPartnerMeta(PartnerMeta mspMeta) {
        this.partnerMeta = mspMeta;
    }


    /**
     * Extra fare if the vehicle is parked in a forbidden area.
     */
    @Column(name = "parking_Forbidden_Fee")
    private Long parkingForbiddenFee;

    /**
     * Extra fare if the vehicle is parked out of bounds.
     */
    @Column(name = "out_Of_Bound_Fee")
    private Long outOfBoundFee;

    @Column(name = "fk_msp_meta")
    public UUID getFkMspMeta() {
        return this.fkMspMeta;
    }

    public void setFkMspMeta(UUID fkMspMeta) {
        this.fkMspMeta = fkMspMeta;
    }


}
