package com.gateway.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "token", schema = "msp")
public class Token implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "token_id", unique = true, nullable = false)
	private UUID tokenId;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "expire_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime expireAt;

	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="partner_id")
	private PartnerMeta partner;
}
