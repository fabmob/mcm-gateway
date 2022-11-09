package com.gateway.database.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"priceListForDuration", "priceListForDistance"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "price_list_item", schema = "msp")
public class PriceListItem implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_list_item_id")
    private UUID priceListItemId;

    /**
     * Lower boundary of the price list item.
     * <p>
     * For example, for prices depending on duration, it's the minimum duration.
     */
    @Column(name = "lower_price_limit")
    private Long lowerPriceLimit;

    /**
     * Upper boundary of the price list item.
     * <p>
     * For example, for prices depending on duration, it's the maximum duration.
     */
    @Column(name = "upper_price_limit")
    private Long upperPriceLimit;

    /**
     * Fixed fare (in cents). Applied if the price list item is concerned.
     */
    @Column(name = "fixed_fare")
    private Long fixedFare;

    /**
     * Fare per unit (in cents, per unit of time or distance). Applied for each unit.
     */
    @Column(name = "fare_per_unit")
    private Long farePerUnit;

    /**
     * Unit (of time in minutes or distance in kilometers) used to apply the fare per unit.
     */
    @Column(name = "unit")
    private Long unit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "price_list_duration")
    private PriceList priceListForDuration;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "price_list_distance")
    private PriceList priceListForDistance;

}
