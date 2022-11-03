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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "partner_calls", schema = "msp")
public class PartnerCalls implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "partner_call_id", unique = true, nullable = false)
	private UUID partnerCallId;

	@Column(name = "url",length = 200)
	private String url;

	@Column(name = "method",length = 45)
	private String method;

	@Column(name = "is_mocked")
	private Integer isMocked;

	@Column(name = "nb_calls")
	private Integer nbCalls;
	
	@Column(name = "execution_order")
	private Integer executionOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	private PartnerActions action;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "body_id")
	private Body body;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "call_id")
	private Set<Headers> headers;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "call_id")
	private Set<Params> params;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "call_id")
	private Set<ParamsMultiCalls> paramsMultiCalls;
	
}
