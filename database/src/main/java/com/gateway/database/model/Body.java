package com.gateway.database.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "body", schema = "msp")
@AllArgsConstructor
@NoArgsConstructor
public class Body implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "body_id", unique = true, nullable = false)
	private UUID bodyId;
	@Column(name = "template",length=255)
	private String template;
	@Column(name = "is_static")
	private Integer isStatic;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "body")
	private MSPCalls call;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "body_id", referencedColumnName = "body_id")
	private Set<BodyParams> bodyParams;

}
