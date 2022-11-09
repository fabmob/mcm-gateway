package com.gateway.api.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Jacksonized
@Data
@AllArgsConstructor
public class HrefLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="Resource's link", example="http://example/api/ressouce/{id}")
	private String href;
	
	public static HrefLink getLinkFromHateoasLink(org.springframework.hateoas.Link link) {
		return new HrefLink(link.getHref());
	}
}