package com.gateway.database.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "headers_values_template", schema = "msp")
@Data
public class HeadersValuesTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "headers_values_template_id", unique = true, nullable = false)
	private UUID headersValuesTemplateId;
	@Column(name = "key",length=30)
	private String key;
	@Column(name = "value",length=255)
	private String value;

}