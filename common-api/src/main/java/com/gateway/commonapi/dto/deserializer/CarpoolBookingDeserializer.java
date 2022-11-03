package com.gateway.commonapi.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gateway.commonapi.dto.api.DriverCarpoolBooking;
import com.gateway.commonapi.dto.api.OneOfCarpoolBookingEventData;
import com.gateway.commonapi.dto.api.PassengerCarpoolBooking;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;


@Slf4j
public class CarpoolBookingDeserializer extends StdDeserializer<OneOfCarpoolBookingEventData> {

    protected CarpoolBookingDeserializer() {
        this((Class<?>) null);
    }

    protected CarpoolBookingDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CarpoolBookingDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public OneOfCarpoolBookingEventData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        Type targetType;
        if (node.has("driver")) {
            log.debug("Parsed JSON holds a driver");
            targetType = DriverCarpoolBooking.class;
        } else if (node.has("passenger")) {
            log.debug("Parsed JSON holds a passenger");
            targetType = PassengerCarpoolBooking.class;
        } else {
            log.info("Parsed JSON holds no driver or passenger");
            return null;
        }

        JavaType jacksonType = deserializationContext.getTypeFactory().constructType(targetType);
        JsonDeserializer<?> deserializer = deserializationContext.findRootValueDeserializer(jacksonType);
        JsonParser nodeParser = node.traverse(jsonParser.getCodec());
        nodeParser.nextToken();
        return (OneOfCarpoolBookingEventData) deserializer.deserialize(nodeParser, deserializationContext);
    }
}