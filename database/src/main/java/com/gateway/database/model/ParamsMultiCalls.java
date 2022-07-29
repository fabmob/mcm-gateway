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

import lombok.Data;


@Entity
@Data
@Table(name = "params_multi_calls", schema = "msp")
public class ParamsMultiCalls implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "params_multi_calls_id", unique = true, nullable = false)
	private UUID paramsMultiCallsId;
	@Column(name = "key",length=35)
	private String key;
	@Column(name = "value_offset",length=5)
	private String valueOffset;
	@Column(name = "init_value",length=10)
	private String initValue;
	@Column(name = "timezone",length=50)
	private String timezone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "call_id")
	private MSPCalls call;

}
