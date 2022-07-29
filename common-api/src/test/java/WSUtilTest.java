import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class WSUtilTest extends UTTestCase {


    /**
     * Test the charset of each HTTP response VERB
     */
    @Test
    public void testMediaTypeCharset() {

        log.info("Test WSUtilTests > testMediaTypeCharset");
        Assertions.assertEquals(MediaType.APPLICATION_JSON_UTF8.getCharset(), WsTestUtil.getMediaType(HttpMethod.GET).getCharset(), "Verify charset for GET");
        Assertions.assertEquals(MediaType.APPLICATION_JSON_UTF8.getCharset(), WsTestUtil.getMediaType(HttpMethod.POST).getCharset(), "Verify charset for POST");
        Assertions.assertNull(WsTestUtil.getMediaType(HttpMethod.DELETE), "Verify charset for DELETE");
        Assertions.assertNull(WsTestUtil.getMediaType(HttpMethod.PUT), "Verify charset for PUT");
        Assertions.assertEquals(MediaType.APPLICATION_JSON_UTF8.getCharset(), WsTestUtil.getMediaType(HttpMethod.PATCH).getCharset(), "Verify charset for PATCH");
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
    }

    @Test
    public void testGetMockHttpServletRequestBuilder(){

       Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("baseUri","uri", HttpMethod.GET,"contentPayload"));
       Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("baseUri","uri", HttpMethod.PUT,"contentPayload"));
       Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("baseUri","uri", HttpMethod.DELETE,"contentPayload"));
       Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("baseUri","uri", HttpMethod.PATCH,"contentPayload"));
       Assertions.assertNotNull(WsTestUtil.getMockHttpServletRequestBuilder("baseUri","uri", HttpMethod.POST,"contentPayload"));
    }

    @Test
    public void testGetResultMatcher(){
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
    }

    @Test
    public void testIgnoreUUID() {
        Assertions.assertEquals( "\"mspId\": \""+WsTestUtil.FAKE_UUID+"\"", WsTestUtil.ignoreUUID("\"mspId\": \"f5e6cc23-740c-44f1-87bb-96762b35490d\""));
    }

    @Test
    public void testIsUUID() {
        Assertions.assertTrue(CommonUtils.isUUID(UUID.randomUUID().toString()));
    }

    @SneakyThrows
    @Test
    public void testExceptions() {
        NotFound errorObject = new NotFound("myLabel","this is a message");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(errorObject);
        Assertions.assertEquals(errorObject.getLabel(), objectMapper.readValue(message, NotFound.class).getLabel());
        Assertions.assertEquals(errorObject.getErrorCode(), objectMapper.readValue(message, NotFound.class).getErrorCode());
        Assertions.assertEquals(errorObject.getDescription(), objectMapper.readValue(message, NotFound.class).getDescription());
        Assertions.assertEquals(errorObject.getTimestamp(), objectMapper.readValue(message, NotFound.class).getTimestamp());
    }

    @Test
    public void testPlaceholderFormat() {
        Assertions.assertEquals("/url/14/id",CommonUtils.placeholderFormat("/url/{placeholder}/id", "placeholder", "14"));
        Assertions.assertEquals("/url/14/id/myValue",CommonUtils.placeholderFormat("/url/{placeholder}/id/{otherVar}", "placeholder", "14", "otherVar", "myValue"));
        Assertions.assertThrowsExactly(IllegalArgumentException.class,() -> CommonUtils.placeholderFormat("/url/{placeholder}/id", "placeholder"));
    }
}
