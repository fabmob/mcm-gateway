package com.gateway.adapter.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomAdapterService {
    
	/**
	 * Retun Mock With a list of objects associated to the Partner
     * @param params      List of parameters
     * @param partnerActionId action identifier
     * @param partnerId       Partner identifier
     * @return list of objects returned by the Partner
     */
    List<Object> adaptOperation(Map<String, String> params, UUID partnerActionId, UUID partnerId, Map<String, Object> originalBody) throws  IOException, IntrospectionException;


}
