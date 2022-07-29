package com.gateway.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
@Getter
@Setter
public class StandardPK implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "version_standard", length = 10)
    private String versionStandard;

    @Column(name = "version_datamapping", length = 10)
    private String versionDataMapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msp_id")
    private MspMeta msp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mspActionId")
    private MSPActions action;
}
