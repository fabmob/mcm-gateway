package com.gateway.adapter.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.exception.InternalException;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatUtilsTest {

    @Test
    void testFormatVaue() throws JSONException {

        String internalField = "test";
        String externalField = "test";
        String timezone = "Europe/Paris";

        Object value1 = 10;
        String format1 = "NUMERIC_OPERATOR(*,100)";
        String format1b = "NUMERIC_OPERATOR(-,1)";
        String format1c = "NUMERIC_OPERATOR(+,1)";
        String format1d = "NUMERIC_OPERATOR(/,1)";
        String expectedLogMsg1 = "Call of function NUMERIC_OPERATOR, with params: *,100.0";
        String str = "[\"air-conditionnning\", \"child-seat\"]";
        Object value2 = new JSONArray(str);
        String format2 = "CONVERT_LIST_TO_STRING(\",\")";
        String expectedLogMsg2 = "Call of function CONVERT_LIST_TO_STRING, with params: \",\"";
        Object value3 = "2022/05/25 15:55:46";
        String format3 = "FORMAT_DATE(\"yyyy/M/d HH:mm:ss\")";
        String expectedLogMsg3 = "Call of function FORMAT_DATE, with params: \"yyyy/M/d HH:mm:ss\"";
        Object value4 = "parking_lot";
        String format4 = "CONVERT_STRING_TO_BOOLEAN(\"parking_lot\"=\"stations.isParkingLot\", \"street_parking\"=\"stations.isStreetParking\")";
        String expectedLogMsg4 = "Call of function CONVERT_STRING_TO_BOOLEAN, with params: [street_parking=stations.isStreetParking, parking_lot=stations.isParkingLot]";

        DataMapperDTO mapperDTO1 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format1, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        DataMapperDTO mapperDTO2 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format2, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        DataMapperDTO mapperDTO3 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format3, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        DataMapperDTO mapperDTO4 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format4, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));

        // get Logback Logger
        Logger logger = (Logger) LoggerFactory.getLogger(FormatUtils.class);

        // create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // add the appender to the logger
        logger.addAppender(listAppender);

        // call method under test
        FormatUtils.formatValue(value1, format1, internalField, timezone, mapperDTO1);
        FormatUtils.formatValue(value2, format2, internalField, timezone, mapperDTO2);
        FormatUtils.formatValue(value3, format3, internalField, timezone, mapperDTO3);
        FormatUtils.formatValue(value4, format4, internalField, timezone, mapperDTO4);

        // JUnit assertions on logs (US#140):
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(expectedLogMsg1, logsList.get(1)
                .getMessage());
        assertEquals(Level.INFO, logsList.get(1)
                .getLevel());

        assertEquals(expectedLogMsg2, logsList.get(3)
                .getMessage());
        assertEquals(Level.INFO, logsList.get(3)
                .getLevel());

        assertEquals(expectedLogMsg3, logsList.get(5)
                .getMessage());
        assertEquals(Level.INFO, logsList.get(5)
                .getLevel());

        assertEquals(expectedLogMsg4, logsList.get(7)
                .getMessage());
        assertEquals(Level.INFO, logsList.get(7)
                .getLevel());


        //assertions on methods results (US#181):
        //NUMERIC_OPERATOR
        Map<String, Object> expectedResult1 = new HashMap<>();
        expectedResult1.put("internalField", internalField);
        Float result = Float.valueOf(1000);
        expectedResult1.put("value", result);
        assertEquals(expectedResult1, FormatUtils.formatValue(value1, format1, internalField, timezone, mapperDTO1));

        Map<String, Object> expectedResult1b = new HashMap<>();
        expectedResult1b.put("internalField", internalField);
        Float resultb = Float.valueOf(9);
        expectedResult1b.put("value", resultb);
        assertEquals(expectedResult1b, FormatUtils.formatValue(value1, format1b, internalField, timezone, mapperDTO1));

        Map<String, Object> expectedResult1c = new HashMap<>();
        expectedResult1c.put("internalField", internalField);
        Float resultc = Float.valueOf(11);
        expectedResult1c.put("value", resultc);
        assertEquals(expectedResult1c, FormatUtils.formatValue(value1, format1c, internalField, timezone, mapperDTO1));

        Map<String, Object> expectedResult1d = new HashMap<>();
        expectedResult1d.put("internalField", internalField);
        Float resultd = Float.valueOf(10);
        expectedResult1d.put("value", resultd);
        assertEquals(expectedResult1d, FormatUtils.formatValue(value1, format1d, internalField, timezone, mapperDTO1));

        assertThrows(InternalException.class, () -> {
            FormatUtils.formatValue("test", format1, internalField, timezone, mapperDTO1);
        });

        //CONVERT_LIST_TO_STRING
        Map<String, Object> expectedResult2 = new HashMap<>();
        expectedResult2.put("internalField", internalField);
        expectedResult2.put("value", "air-conditionnning,child-seat");
        assertEquals(expectedResult2, FormatUtils.formatValue(value2, format2, internalField, timezone, mapperDTO2));

        assertThrows(InternalException.class, () -> {
            FormatUtils.formatValue("test", format2, internalField, timezone, mapperDTO2);
        });

        //FORMAT_DATE
        Map<String, Object> expectedResult3 = new HashMap<>();
        expectedResult3.put("internalField", internalField);
        OffsetDateTime date = OffsetDateTime.parse("2022-05-25T13:55:46Z");
        expectedResult3.put("value", date);
        assertEquals(expectedResult3, FormatUtils.formatValue(value3, format3, internalField, timezone, mapperDTO3));

        Map<String, Object> expectedResult3b = new HashMap<>();
        expectedResult3b.put("internalField", internalField);
        ZonedDateTime dateb = ZonedDateTime.parse("2020-07-04T16:00:00Z");
        expectedResult3b.put("value", dateb);
        assertEquals(expectedResult3b, FormatUtils.formatValue(1593878400, "FORMAT_DATE(\"timestamp\")", internalField, timezone, mapperDTO3));

        assertThrows(InternalException.class, () -> {
            FormatUtils.formatValue("test", format3, internalField, timezone, mapperDTO3);
        });


        //CONVERT_STRING_TO_BOOL
        Map<String, Object> expectedResult4 = new HashMap<>();
        expectedResult4.put("internalField", "stations.isParkingLot");
        expectedResult4.put("value", true);
        assertEquals(expectedResult4, FormatUtils.formatValue(value4, format4, internalField, timezone, mapperDTO4));


    }

    @Test
    void testFormatVaueException() {

        Object value = "test";
        String internalField = "test";
        String externalField = "test";
        String timezone = "yyyy/M/d HH:mm:ss";

        String format1 = "FUNCTION(x,y)";
        String format2 = "CONVERT_STRING_TO_BOOLEAN(\"parking_lot\"==\"stations.isParkingLot\", \"street_parking\"=\"stations.isStreetParking\"=1)";
        DataMapperDTO mapperDTO1 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format1, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        DataMapperDTO mapperDTO2 = new DataMapperDTO(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"), internalField, externalField, 0, format2, timezone, null, null, UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));

        assertThrows(InternalException.class, () -> {
            FormatUtils.formatValue(value, format1, internalField, timezone, mapperDTO1);
        });

        assertThrows(InternalException.class, () -> {
            FormatUtils.formatValue(value, format2, internalField, timezone, mapperDTO2);
        });
    }

}