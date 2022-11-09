package com.gateway.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "params", schema = "msp")
public class Params implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "params_id", unique = true, nullable = false)
	private UUID paramsId;

	@Column(name = "key",length=30)
	private String key;
	@Column(name = "sensitive")
	private Integer sensitive;
	@Column(name = "value",length=255)
	private String value;
	@Column(name = "key_mapper",length=50)
	private String keyMapper;
	@Column(name = "precision",length=5)
	private String precision;
	@Column(name = "timezone",length=50)
	private String timezone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "call_id")
	private PartnerCalls call;

}
