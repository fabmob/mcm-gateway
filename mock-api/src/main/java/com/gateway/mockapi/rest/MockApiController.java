package com.gateway.mockapi.rest;


import com.gateway.commonapi.utils.SanitizorUtils;
import com.gateway.mockapi.service.MockApiService;
import com.gateway.mockapi.utils.HttpServletRequestUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import static com.gateway.mockapi.utils.constant.MockApiMessageDict.*;
import static com.gateway.mockapi.utils.constant.MockApiPathDict.MOCK_API_PATH;
import static com.gateway.mockapi.utils.constant.MockApiPathDict.MOCK_OPERATION_NAME;


@Slf4j
@Validated
@RestController
@RequestMapping(MOCK_API_PATH)
public class MockApiController {

    public static final String MOCK_PATH = "mock-path";
    public static final String MOCK_DELAY = "mock-delay";
    public static final String DEFAULT_DELAY = "10";
    @Autowired
    private MockApiService service;

    /**
     * Returns a mocked response to simulate a MSP or a Mass.
     * <p>
     * Extracts from the specific mandatory header 'mock-path' the response code and an optional response body.
     * The mockPath is a relative path from a mock root directory, and can contain multiple subdirectories. The last subdirectory MUST be named with the responseCode.
     * If mockPath ends with a filename and extension, a body is returned with that file content.
     * <p>
     * Examples :
     * header 'mock-path'='partner1/case2/201' -> response code 201 without body
     * header 'mock-path'='partner1/case2/200/body.json' -> response code 209 with body.json content
     *
     * @param request  the request containing at least the mock response path
     * @param setDelay apply a delay before execute the request (in milliseconds)
     * @return required response code and an optional body
     */
    private ResponseEntity<String> execute(HttpServletRequest request, String mockPath, Integer setDelay) throws InterruptedException {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestParameters = HttpServletRequestUtils.getParametersAsString(request);
        String requestHeaders = HttpServletRequestUtils.getHeadersAsString(request);
        String requestBody = HttpServletRequestUtils.getBodyAsString(request);

        log.info(MessageFormat.format(LOG_MOCKS_REQUEST_PATTERN, requestMethod, SanitizorUtils.convertToSingleLine(mockPath), requestParameters, requestHeaders, StringUtils.isNotEmpty(requestBody) ? requestBody : LOG_MOCKS_NO_BODY));

        String body = service.getMockedBody(mockPath);
        int responseCode = service.getMockedResponseCode(mockPath);

        log.info(MessageFormat.format(LOG_MOCKS_RESPONSE_PATTERN, requestMethod, SanitizorUtils.convertToSingleLine(mockPath), responseCode, StringUtils.isNotEmpty(body) ? SanitizorUtils.convertToSingleLine(body) : LOG_MOCKS_NO_BODY));

        if (setDelay < 0) {
            setDelay = Integer.valueOf(DEFAULT_DELAY);
        }
        TimeUnit.MILLISECONDS.sleep(setDelay);
        return new ResponseEntity<>(body, HttpStatus.valueOf(responseCode));
    }

    @Operation(summary = "Mock a MSP or a MaaS", description = "Use 'mock-path' header to respond with a return code and an optional body. A value like 'case/200/ok.json' will respond a HTTP 200 with the content of case/200/OK.json file in the body.")
    @GetMapping(value = MOCK_OPERATION_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executeGet(HttpServletRequest request,
                                             @RequestHeader(name = MOCK_PATH) String mockPath,
                                             @RequestHeader(name = MOCK_DELAY, required = false, defaultValue = DEFAULT_DELAY) Integer setDelay) throws InterruptedException {
        return execute(request, mockPath, setDelay);
    }

    @Operation(summary = "Mock a MSP or a MaaS", description = "Use 'mock-path' header to respond with a return code and an optional body. A value like 'case/200/ok.json' will respond a HTTP 200 with the content of case/200/OK.json file in the body.")
    @PostMapping(value = MOCK_OPERATION_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executePost(HttpServletRequest request,
                                              @RequestHeader(name = MOCK_PATH) String mockPath,
                                              @RequestHeader(name = MOCK_DELAY, required = false, defaultValue = DEFAULT_DELAY) Integer setDelay) throws InterruptedException {
        return execute(request, mockPath, setDelay);
    }

    @Operation(summary = "Mock a MSP or a MaaS", description = "Use 'mock-path' header to respond with a return code and an optional body. A value like 'case/200/ok.json' will respond a HTTP 200 with the content of case/200/OK.json file in the body.")
    @PutMapping(value = MOCK_OPERATION_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executePut(HttpServletRequest request,
                                             @RequestHeader(name = MOCK_PATH) String mockPath,
                                             @RequestHeader(name = MOCK_DELAY, required = false, defaultValue = DEFAULT_DELAY) Integer setDelay) throws InterruptedException {
        return execute(request, mockPath, setDelay);
    }

    @Operation(summary = "Mock a MSP or a MaaS", description = "Use 'mock-path' header to respond with a return code and an optional body. A value like 'case/200/ok.json' will respond a HTTP 200 with the content of case/200/OK.json file in the body.")
    @PatchMapping(value = MOCK_OPERATION_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executePatch(HttpServletRequest request,
                                               @RequestHeader(name = MOCK_PATH) String mockPath,
                                               @RequestHeader(name = MOCK_DELAY, required = false, defaultValue = DEFAULT_DELAY) Integer setDelay) throws InterruptedException {
        return execute(request, mockPath, setDelay);
    }

    @Operation(summary = "Mock a MSP or a MaaS", description = "Use 'mock-path' header to respond with a return code and an optional body. A value like 'case/200/ok.json' will respond a HTTP 200 with the content of case/200/OK.json file in the body.")
    @DeleteMapping(value = MOCK_OPERATION_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executeDelete(HttpServletRequest request,
                                                @RequestHeader(name = MOCK_PATH) String mockPath,
                                                @RequestHeader(name = MOCK_DELAY, required = false, defaultValue = DEFAULT_DELAY) Integer setDelay) throws InterruptedException {
        return execute(request, mockPath, setDelay);
    }

}
