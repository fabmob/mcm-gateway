import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gateway.commonapi.utils.CommonUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class WSUtilTest extends UTTestCase {


    @MockBean
    ObjectMapper mockObjectMapper;

    @PostConstruct
    public void initMock() throws JsonProcessingException {
        when(mockObjectMapper.writeValueAsString(any(JSONObject.class))).thenThrow(JsonProcessingException.class);
    }

    /**
     * Test the charset of each HTTP response VERB
     */
    @Test
    public void testMediaTypeCharset() {

        log.info("Test WSUtilTests > testMediaTypeCharset");
        Assertions.assertEquals(MediaType.APPLICATION_JSON.getCharset(), WsTestUtil.getMediaType(HttpMethod.GET).getCharset(), "Verify charset for GET");
        Assertions.assertEquals(MediaType.APPLICATION_JSON.getCharset(), WsTestUtil.getMediaType(HttpMethod.POST).getCharset(), "Verify charset for POST");
        Assertions.assertNull(WsTestUtil.getMediaType(HttpMethod.DELETE), "Verify charset for DELETE");
        Assertions.assertNull(WsTestUtil.getMediaType(HttpMethod.PUT), "Verify charset for PUT");
        Assertions.assertEquals(MediaType.APPLICATION_JSON.getCharset(), WsTestUtil.getMediaType(HttpMethod.PATCH).getCharset(), "Verify charset for PATCH");
    }

    @Test
    public void testReadJsonFromFilePath() throws IOException {
        log.info("Test WSUtilTests > readJsonFromFilePath");
        Assertions.assertEquals("{\"message\": \"sample Json file\"}", WsTestUtil.readJsonFromFilePath("src/test/resources/sample.json"));
    }

    @Test
    public void testContentTypeResultMatcher() {
        log.info("Test WSUtilTests > getContentTypeResultMatcher");
        Assertions.assertNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.NOT_FOUND));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.OK));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.CREATED));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.NO_CONTENT));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.NOT_IMPLEMENTED));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.BAD_GATEWAY));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.BAD_REQUEST));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.UNAUTHORIZED));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.FORBIDDEN));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.METHOD_NOT_ALLOWED));
        Assertions.assertNotNull(WsTestUtil.getContentTypeResultMatcher(MediaType.APPLICATION_JSON, HttpStatus.CONFLICT));

    }

    @Test
    public void testGetMockHttpServletRequestBuilder() {

        Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("http://baseUri", "uri", HttpMethod.GET, "contentPayload"));
        Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("http://baseUri", "uri", HttpMethod.PUT, "contentPayload"));
        Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("http://baseUri", "uri", HttpMethod.DELETE, "contentPayload"));
        Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("http://baseUri", "uri", HttpMethod.PATCH, "contentPayload"));
        Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("http://baseUri", "uri", HttpMethod.POST, "contentPayload"));
    }

    @Test
    public void testGetResultMatcher() {
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.OK));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.CREATED));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.NOT_FOUND));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.NO_CONTENT));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.NOT_IMPLEMENTED));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.BAD_GATEWAY));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.BAD_REQUEST));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.UNAUTHORIZED));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.FORBIDDEN));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.METHOD_NOT_ALLOWED));
        Assertions.assertNotNull(WsTestUtil.getResultMatcher(HttpStatus.CONFLICT));
    }

    @Test
    public void testIgnoreUUID() {
        Assertions.assertEquals("\"mspId\": \"" + WsTestUtil.FAKE_UUID + "\"", WsTestUtil.ignoreUUID("\"mspId\": \"f5e6cc23-740c-44f1-87bb-96762b35490d\""));
    }

    @Test
    public void testIsUUID() {
        Assertions.assertTrue(CommonUtils.isUUID(UUID.randomUUID().toString()));
    }

    @SneakyThrows
    @Test
    public void testExceptions() {
        NotFound errorObject = new NotFound("myLabel", "this is a message");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(errorObject);
        Assertions.assertEquals(errorObject.getLabel(), objectMapper.readValue(message, NotFound.class).getLabel());
        Assertions.assertEquals(errorObject.getErrorCode(), objectMapper.readValue(message, NotFound.class).getErrorCode());
        Assertions.assertEquals(errorObject.getDescription(), objectMapper.readValue(message, NotFound.class).getDescription());
        Assertions.assertEquals(errorObject.getTimestamp(), objectMapper.readValue(message, NotFound.class).getTimestamp());
    }

    @Test
    public void testPlaceholderFormat() {
        Assertions.assertEquals("/url/14/id", CommonUtils.placeholderFormat("/url/{placeholder}/id", "placeholder", "14"));
        Assertions.assertEquals("/url/14/id/myValue", CommonUtils.placeholderFormat("/url/{placeholder}/id/{otherVar}", "placeholder", "14", "otherVar", "myValue"));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> CommonUtils.placeholderFormat("/url/{placeholder}/id", "placeholder"));
    }

    @Test
    public void testSetHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Correlation-id", new ThreadLocalUserSession().get().getContextId());

        Assertions.assertEquals(httpHeaders, setHttpHeaders());
    }

    @Test
    public void testStringifyGenericErrorDto() throws JsonProcessingException {
        GenericError genericError = new GenericError();
        genericError.setErrorCode(404);
        genericError.setDescription("Not Found resource");
        genericError.setLabel("Not found");
        genericError.setTimestamp("2022-07-12 17:10:45.6478667");
        genericError.setCallId(UUID.fromString("b6c92466-586b-4425-a186-bdb240325941"));

        ObjectMapper objectMapper = new ObjectMapper();

        String expectedError = "{\"errorCode\":404," +
                "\"label\":\"Not found\"" +
                ",\"description\":\"Not Found resource\"" +
                ",\"timestamp\":\"2022-07-12 17:10:45.6478667\"" +
                ",\"callId\":\"b6c92466-586b-4425-a186-bdb240325941\"}";

        System.out.println(objectMapper.writeValueAsString(genericError));
        Assertions.assertEquals(objectMapper.writeValueAsString(genericError), stringifyGenericErrorDto(genericError));
        Assertions.assertEquals(expectedError, stringifyGenericErrorDto(genericError));
    }

    @Test
    public void testSetHeader() {
        String correlationId = new ThreadLocalUserSession().get().getContextId();
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        Assertions.assertEquals(httpEntity, setHeaders());
    }


    @Test
    public void testThreadLocalUserSession() {

        String initialThreadName = Thread.currentThread().getName();
        ThreadLocalUserSession session = new ThreadLocalUserSession();
        String previousContextId = session.get().getContextId();
        log.info("Step1 Context id => {} for name={}", session.get().getContextId(), Thread.currentThread().getName());
        log.info("run");
        session.run();
        Assertions.assertEquals(initialThreadName, Thread.currentThread().getName());
        Assertions.assertNotEquals(previousContextId, session.get().getContextId(), "Context Id should have changed");

        String newContextId = session.get().getContextId();
        log.info("Step2 Context id => {} for name={}", session.get().getContextId(), Thread.currentThread().getName());
        log.info("saveContext");
        session.saveContext();
        Assertions.assertEquals(initialThreadName, Thread.currentThread().getName());
        Assertions.assertEquals(newContextId, session.get().getContextId(), "Context Id should keep same as before saveContext");

        log.info("Step3 Context id => {} for name={}", session.get().getContextId(), Thread.currentThread().getName());
        log.info("unload");
        session.unload();
        Assertions.assertEquals(initialThreadName, Thread.currentThread().getName());
        log.info("Step4 Context id => {} for name={}", session.get().getContextId(), Thread.currentThread().getName());
    }


    @Test
    public void testProcessFunction() {
        String processFunction = "BASE64";
        String value = "myValue";

        String finalValue = "bXlWYWx1ZQ==";

        Assertions.assertEquals(finalValue, processFunction(value, processFunction));

        Assertions.assertEquals(value, processFunction(value, ""));
    }

    @Test
    public void testConstructUrlTemplate() {
        String urlCall = "http://localhost:8081/v1/adapters/addAdapter";
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("key", "value");

        String expectedUrlTemplate = "http://localhost:8081/v1/adapters/addAdapter?cl%25C3%25A9=valeur&key=value";

        Assertions.assertEquals(expectedUrlTemplate, constructUrlTemplate(urlCall, params));

        String invalidUri = "";
        Assertions.assertEquals(invalidUri, constructUrlTemplate(invalidUri, params));
    }

    @Test
    public void testIsValidFunction() {
        Assertions.assertEquals(true, isValidFunction("NUMERIC_OPERATOR(*,\"100\")"));
        Assertions.assertEquals(true, isValidFunction("NUMERIC_OPERATOR(*,100)"));
        Assertions.assertEquals(true, isValidFunction("CONVERT_STRING_TO_BOOLEAN(\"parking_lot\"=\"isParkingLot\",\"street_parking\"=\"isStreetParking\",\"underground_parking\"=\"isUnderground\",\"sidewalk_parking\"=\"isSidewalkParking\")"));
        Assertions.assertEquals(true, isValidFunction("CONVERT_STRING_TO_BOOLEAN(parking_lot = isParkingLot,street_parking = isStreetParking)"));
        Assertions.assertEquals(true, isValidFunction("FORMAT_DATE(\"timestamp\")"));
        Assertions.assertEquals(true, isValidFunction("CONVERT_LIST_TO_STRING(\", \")"));

        Assertions.assertEquals(false, isValidFunction("FALSE_FUNCTION(\", \")"));
        Assertions.assertEquals(false, isValidFunction("NUMERIC_OPERATOR(*,toto)"));
        Assertions.assertEquals(false, isValidFunction("NUMERIC_OPERATOR(*,100\")"));
        Assertions.assertEquals(false, isValidFunction("NUMERIC_OPERATOR(*,\"100)"));
        Assertions.assertEquals(false, isValidFunction("NUMERIC_OPERATOR(*,10\"0)"));
        Assertions.assertEquals(false, isValidFunction("CONVERT_STRING_TO_BOOLEAN(parking_lot = \"isParkingLot,street_parking = isStreetParking)"));

    }


}
