package com.gateway.database.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "headers", schema = "msp")
@Getter
@Setter
public class Headers implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "headers_id", unique = true, nullable = false)
	private UUID headersId;
	@Column(name = "key", length = 30)
	private String key;
	@Column(name = "sensitive")
	private Integer sensitive;
	@Column(name = "value", length = 255)
	private String value;
	@Column(name = "process_function", length = 10)
	private String processFunction;
	@Column(name = "security_flag")
	private Integer securityFlag;

	@Column(name = "value_prefix", length = 10)
	private String valuePrefix;
	@Column(name = "value_template", length = 40)
	private String valueTemplate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "headers_id")
	private Set<HeadersValuesTemplate> headersValuesTemplate;

}