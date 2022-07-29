package com.gateway.adapter.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomAdapterService {
    
	/**
	 * Retun Mock With a list of objects associated to the MSP
     * @param params      List of parameters
     * @param mspActionId action identifier
     * @param mspId       Msp identifier
     * @return list of objects returned by the msp
     */
    List<Object> adaptOperation(Map<String, String> params, UUID mspActionId, UUID mspId, Map<String, Object> originalBody) throws  IOException, IntrospectionException;


}
