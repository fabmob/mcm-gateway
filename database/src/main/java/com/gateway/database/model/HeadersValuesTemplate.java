package com.gateway.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

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