package com.gateway.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

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
	private PartnerCalls call;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "body_id", referencedColumnName = "body_id")
	private Set<BodyParams> bodyParams;

}
