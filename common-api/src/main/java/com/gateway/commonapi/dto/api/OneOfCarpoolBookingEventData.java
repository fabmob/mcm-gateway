package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gateway.commonapi.dto.deserializer.CarpoolBookingDeserializer;

/**
 * OneOfCarpoolBookingEventData
 */

@JsonDeserialize(using = CarpoolBookingDeserializer.class)
public interface OneOfCarpoolBookingEventData {

}
