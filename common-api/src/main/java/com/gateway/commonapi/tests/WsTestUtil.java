package com.gateway.commonapi.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.gateway.commonapi.constants.GlobalConstants;
import org.apache.commons.io.IOUtils;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Utility tools to implement IT tests for web services.
 */
public class WsTestUtil {

    public static final String FAKE_UUID = "FAKE_UUID";
    public static final String FAKE_TIMESTAMP = "2080-01-01T00:00:00.154+01:00";
    public static final String UUID_REGEXP = "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";

    private WsTestUtil() {}

    /**
     * Convert a Path file into a String
     * @param pathFile file path
     * @return Content of the file as String
     * @throws IOException IO Exception reading file
     */
    public static String readJsonFromFilePath(String pathFile) throws IOException {
        File fileRef = new File(pathFile);
        String jsonRef = "";
        if (fileRef.exists()) {
            InputStream stream = new FileInputStream(pathFile);
            String jsonStringyfied = IOUtils.toString(stream, StandardCharsets.UTF_8);
            // replace multiple spaces into single one
            jsonRef = jsonStringyfied.trim().replaceAll("\\r|\\n","").replaceAll("\\s{2,}+", "");
        }

        return jsonRef;
    }

    /**
     * Prepare the MockHttpServletRequestBuilder to simulate a web service call
     * @param baseUri basePath of the web service (eg : /v1)
     * @param uri operation path to call
     * @param httpMethod verb of the http request (GET,PUT, POST, DELETE, PATCH, ...)
     * @param requestPayloadContent If not empty the payload to inject into the request body
     * @return MockHttpServletRequestBuilder
     */
    public static MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(String baseUri, String uri, HttpMethod httpMethod, String requestPayloadContent) {
        MockHttpServletRequestBuilder requestBuilder = null;
        String urlRequest = baseUri + uri;
        switch (httpMethod) {
            case GET:
                requestBuilder = MockMvcRequestBuilders
                        .get(urlRequest);
                break;
            case PUT:
                requestBuilder = MockMvcRequestBuilders
                        .put(urlRequest).content(requestPayloadContent).contentType(MediaType.APPLICATION_JSON);
                break;
            case POST:
                requestBuilder = MockMvcRequestBuilders
                        .post(urlRequest).content(requestPayloadContent).contentType(MediaType.APPLICATION_JSON);
                break;
            case DELETE:
                requestBuilder = MockMvcRequestBuilders
                        .delete(urlRequest);
                break;
            case PATCH:
                requestBuilder = MockMvcRequestBuilders
                        .patch(urlRequest).content(requestPayloadContent).contentType(MediaType.APPLICATION_JSON);
                break;
            default:
                break;
        }
        return requestBuilder;
    }

    /**
     * Return the expected response MediaType regarding the httpMethod used.
     * @param httpMethod GET/PUT/POST/DELETE/PATCH
     * @return
     */
    public static MediaType getMediaType(HttpMethod httpMethod) {
        MediaType mediaType;
        switch (httpMethod) {
            case GET:
            case POST:
            case PATCH:
                mediaType = MediaType.APPLICATION_JSON_UTF8;
                break;
            case PUT:
            case DELETE:
            default:
                mediaType = null;
                break;
        }
        return mediaType;
    }

    /**
     * Prepare the test ResultMatcher to test call response status regarding the HTTP response code expected.
     * @param httpStatusExpectedResult expected http status code
     * @return the {@link ResultMatcher} to use for test
     */
    public static ResultMatcher getResultMatcher(HttpStatus httpStatusExpectedResult) {
        ResultMatcher mockResultMatcher = null;

        switch (httpStatusExpectedResult) {
            case OK:
                mockResultMatcher = MockMvcResultMatchers.status().isOk();
                break;
            case CREATED:
                mockResultMatcher = MockMvcResultMatchers.status().isCreated();
                break;
            case NOT_FOUND:
                mockResultMatcher = MockMvcResultMatchers.status().isNotFound();
                break;
            case SERVICE_UNAVAILABLE:
                mockResultMatcher = MockMvcResultMatchers.status().isServiceUnavailable();
                break;
            case NO_CONTENT:
                mockResultMatcher = MockMvcResultMatchers.status().isNoContent();
                break;
            case NOT_IMPLEMENTED:
                mockResultMatcher = MockMvcResultMatchers.status().isNotImplemented();
                break;
            case BAD_GATEWAY:
                mockResultMatcher = MockMvcResultMatchers.status().isBadGateway();
                break;
            case BAD_REQUEST:
                mockResultMatcher = MockMvcResultMatchers.status().isBadRequest();
                break;
            case UNAUTHORIZED:
                mockResultMatcher = MockMvcResultMatchers.status().isUnauthorized();
                break;
            case FORBIDDEN:
                mockResultMatcher = MockMvcResultMatchers.status().isForbidden();
                break;
            case INTERNAL_SERVER_ERROR:
                mockResultMatcher = MockMvcResultMatchers.status().isInternalServerError();
                break;
            case METHOD_NOT_ALLOWED:
                mockResultMatcher = MockMvcResultMatchers.status().isMethodNotAllowed();
                break;
            default:
                break;
        }
        return mockResultMatcher;
    }

    /**
     * Prepare the test ResultMatcher to test contentType regarding the HTTP response code expected and responseContentType
     * @param responseContentType Content type expected
     * @param httpStatusExpectedResult Http status code expected as result of the call
     * @return @{@link ResultMatcher} to test contentType
     */
    public static ResultMatcher getContentTypeResultMatcher(MediaType responseContentType, HttpStatus httpStatusExpectedResult) {

        ResultMatcher contentTypeResultMatcher ;

        switch (httpStatusExpectedResult) {
            case NOT_FOUND:
                contentTypeResultMatcher = null;
                break;
            case OK:
            case CREATED:
            case NO_CONTENT:
            case NOT_IMPLEMENTED:
            case BAD_GATEWAY:
            case BAD_REQUEST:
            case UNAUTHORIZED:
            case FORBIDDEN:
            case INTERNAL_SERVER_ERROR:
            case METHOD_NOT_ALLOWED:
            default:
                contentTypeResultMatcher = MockMvcResultMatchers.content().contentType(responseContentType);
                break;
        }

        return contentTypeResultMatcher;
    }

    /**
     * Replace UUID in a json with a fake string
     * @param jsonStringyfied Json where to replace UUID
     * @return the modified json with no UUID anymore
     */
    public static String ignoreUUID(String jsonStringyfied) {
        return jsonStringyfied.replaceAll(GlobalConstants.UUID_REGEXP, FAKE_UUID);
    }

    /**
     * Replace TimeStamps with fake one
     * @param jsonStringyfied
     * @return the modified json with mocked timestamps
     */
    public static String ignoreTimeStamp(String jsonStringyfied) {
        return jsonStringyfied.replaceAll(GlobalConstants.ISO8601_DATE_REGEXP, FAKE_TIMESTAMP);
    }
}
