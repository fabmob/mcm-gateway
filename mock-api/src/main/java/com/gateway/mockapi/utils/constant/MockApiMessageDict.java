package com.gateway.mockapi.utils.constant;

public class MockApiMessageDict {

    /**
     * private constructor
     */
    private MockApiMessageDict() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LOG_MOCKS_REQUEST_PATTERN = "Requested {0} with mock-path {1} | request parameters {2} | headers {3} | body : {4}";
    public static final String LOG_MOCKS_RESPONSE_PATTERN = "Responding to the {0} with mock-path {1} | response code {2} | body : {3}";
    public static final String LOG_MOCKS_NO_BODY = "none";
    public static final String ERROR_MOCKS_NO_RESPONSE_CODE = "The last subdirectory must be named with an integer response code";
    public static final String ERROR_MOCKS_DURING_FILE_READ = "Could not read mock-path  qfor callId {0} : {1}";
    public static final String ERROR_MOCKS_NO_ROOT_DIR = "Mock root directory is not set";
    public static final String ERROR_MOCKS_NO_HEADER = "The mock-path header is mandatory";
    public static final String ERROR_MOCKS_INCORRECT_PATH = "The mock-path must start with a letter or number. Any other char is forbidden";
    public static final String ERROR_MOCKS_PATH_TRAVERSAL = "The mock-path header must be a sub directory or file";
    public static final String ERROR_MOCKS_HEADER_UNUSABLE = "The mock-path header is malformed";
}
