package com.gateway.database.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "body_params", schema = "msp")
@Getter
@Setter
public class BodyParams implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "body_params_id", unique = true, nullable = false)
	private UUID bodyParamsId;
	@Column(name = "key_mapper",length=50)
	private String keyMapper;
	@Column(name = "key",length=30)
	private String key;
	@Column(name = "sensitive")
	private Integer sensitive;
	@Column(name = "value",length=255)
	private String value;
	@Column(name = "precision",length=5)
	private String precision;
	@Column(name = "timezone",length=50)
	private String timezone;
	@Column(name = "is_refresh_token")
	private Integer isRefreshToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="body_id")
	private Body body;


}