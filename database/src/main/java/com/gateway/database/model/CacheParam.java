package com.gateway.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "cache_param", schema = "msp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CacheParam implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name = "cache_param_id", unique = true, nullable = false)
    private UUID cacheParamId;

    @EmbeddedId
    private CacheParamPK cacheParamPK;

    @Column(name = "soft_ttl")
    private Integer softTTL;

    @Column(name = "hard_ttl")
    private Integer hardTTL;

    @Column(name = "refresh_cache_delay")
    private Integer refreshCacheDelay;
}

