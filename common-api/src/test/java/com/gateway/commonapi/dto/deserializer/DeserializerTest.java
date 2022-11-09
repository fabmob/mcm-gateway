package com.gateway.commonapi.dto.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.dto.api.CarpoolBookingEvent;
import com.gateway.commonapi.dto.api.DriverCarpoolBooking;
import com.gateway.commonapi.dto.api.PassengerCarpoolBooking;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;


@Slf4j
public class DeserializerTest {

    private ObjectMapper mapper;


    @Before
    public void setup() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CarpoolBookingEvent createMockCarpoolBookingEvent(String userType) throws JsonProcessingException {
        String json = "{\n" +
                "  \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                "  \"idToken\": \"stringtokenid\",\n" +
                "  \"data\": {\n" +
                "    \"id\": \"booking00321\",\n" +
                "    \"passengerPickupDate\": 1657092126,\n" +
                "    \"passengerPickupLat\": 2.293444,\n" +
                "    \"passengerPickupLng\": 48.858997,\n" +
                "    \"passengerDropLat\": 1.443332,\n" +
                "    \"passengerDropLng\": 43.604583,\n" +
                "    \"passengerPickupAddress\": \"Pont d'Iéna, Paris\",\n" +
                "    \"passengerDropAddress\": \"Place du Capitole, Toulouse\",\n" +
                "    \"status\": \"WAITING_CONFIRMATION\",\n" +
                "    \"distance\": 700000,\n" +
                "    \"duration\": 700001,\n" +
                "    \"webUrl\": \"carpool.mycity.com/booking/booking00321\",\n" +
                "    \"" + userType + "\": {\n" +
                "      \"id\": \"user987654\",\n" +
                "      \"operator\": \"'carpool.mycity.com\",\n" +
                "      \"alias\": \"JeanD\",\n" +
                "      \"firstName\": \"Jean\",\n" +
                "      \"lastName\": \"Dupond\",\n" +
                "      \"grade\": 4,\n" +
                "      \"picture\": \"carpool.mycity.com/user/user987654/picture.jpg\",\n" +
                "      \"gender\": \"F\",\n" +
                "      \"verifiedIdentity\": true\n" +
                "    },\n" +
                "    \"price\": {\n" +
                "      \"type\": \"PAYING\",\n" +
                "      \"amount\": 15.3,\n" +
                "      \"currency\": \"EUR\"\n" +
                "    },\n" +
                "    \"car\": {\n" +
                "      \"model\": \"Model Y\",\n" +
                "      \"brand\": \"Tesla\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<CarpoolBookingEvent>() {
        });
        return objectReader.readValue(json);
    }


    @Test
    public void testCarpoolEventDeserializer() throws JsonProcessingException {
        CarpoolBookingEvent carpoolBookingEvent = this.createMockCarpoolBookingEvent("driver");
        assertThat(carpoolBookingEvent.getData(), instanceOf(DriverCarpoolBooking.class));

        CarpoolBookingEvent carpoolBookingEvent2 = this.createMockCarpoolBookingEvent("passenger");
        assertThat(carpoolBookingEvent2.getData(), instanceOf(PassengerCarpoolBooking.class));

        CarpoolBookingEvent carpoolBookingEvent3 = this.createMockCarpoolBookingEvent("shouldFail");
        assertNull(carpoolBookingEvent3.getData());
    }
}
