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
@Table(name = "data_mapper", schema = "msp")
public class DataMapper implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "data_mapper_id", unique = true, nullable = false)
	private UUID dataMapperId;

	@Column(name = "champ_interne",length=30)
	private String champInterne;

	@Column(name = "access_token",length=150)
	private String champExterne;

	@Column(name = "is_array")
	private Integer isArray;

	@Column(name = "format",length=25)
	private String format;

	@Column(name = "timezone",length=50)
	private String timezone;

	@Column(name = "contained_value",length=30)
	private String containedValue;

	@Column(name = "default_value",length = 200)
	private String defaultValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	private MSPActions action;
	

}
