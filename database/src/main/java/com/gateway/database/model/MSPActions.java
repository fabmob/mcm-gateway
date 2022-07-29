package com.gateway.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "msp_actions", schema = "msp")
public class MSPActions implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "msp_action_id", unique = true, nullable = false)
    private UUID mspActionId;

    @Column(name = "name", length = 25)
    private String name;

    @Column(name = "action", length = 35)
    private String action;

    @Column(name = "is_authentication")
    private boolean isAuthentication;

    @Column(name = "is_refresh_authentication")
    private Integer isRefreshAuth;

    @Column(name = "is_pagination")
    private Integer isPagination;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "selector_id")
    private Selector selector;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private Set<MSPCalls> mspCalls;

    @OneToMany(mappedBy = "action", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<DataMapper> dataMapper;

    @OneToMany(mappedBy = "id.action",cascade = CascadeType.REMOVE)
    private Set<MspStandard> standard;

}
