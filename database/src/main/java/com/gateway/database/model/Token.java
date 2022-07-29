package com.gateway.database.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name="msp_id")
	private MspMeta msp;
}
