package com.gateway.commonapi.constants;

public class ControllerMessageDict {
    private ControllerMessageDict() {
    }

    public static final String RESPONSE_OK = "Response OK";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String NOT_FOUND = "Not Found";
    public static final String INTERNAL_SERVER_ERROR = "Gateway Error";
    public static final String BAD_GATEWAY = "Bad Gateway";
    public static final String CONFLICT = "Conflict";

    public static final String UPDATE_OK = "Updated successfully No content";
    public static final String UPDATE_WITH_BODY = "Updated successfully";


    //TOMP messages
    public static final String RESPONSE_OK_PRICING_PLAN = "returns standard pricing plans for an operator";
    public static final String SUCCESSFUL_OPERATION = "successful operation";
    public static final String TOMP_HEADERS_DESCRIPTION = "The language/localization of user-facing content";
    public static final String TOMP_BAD_REQUEST = "Bad request. See https://github.com/TOMP-WG/TOMP-API/wiki/Error-handling-in-TOMP for further explanation of error code.";
    public static final String TOMP_UNAUTHENTICATED = "Although the HTTP standard specifies \"unauthorized\", semantically this response means \"unauthenticated\". That is, the client must authenticate itself to get the requested response.";
    public static final String TOMP_UNAUTHORIZED = "The client does not have access rights to the content, i.e. they are unauthorized, so server is rejecting to give proper response. Unlike 401, the client's identity is known to the server.";
    public static final String TOMP_NOT_FOUND = "The requested resources does not exist or the requester is not authorized to see it or know it exists.";
    public static final String NOT_EVERY_ENDPOINT_FUNCTIONS_PROPERLY = "not every endpoint functions properly";


    // Carpool messages
    public static final String COVOITURAGE_POST_BOOKING_RESPONSE_OK = "Successful operation. A new Booking has been created.";
    public static final String COVOITURAGE_GET_BOOKING_RESPONSE_OK = "Successful operation.";
    public static final String COVOITURAGE_MESSAGE_RESPONSE_OK = "Successful operation.";
    public static final String COVOITURAGE_IV_RESPONSE_OK = "Ok. Request processed successfully.";
    public static final String COVOITURAGE_BAD_REQUEST = "Bad Request. See error message.";
    public static final String COVOITURAGE_UNAUTHORIZED = "Unauthorized. You must authenticate.";
    public static final String COVOITURAGE_GET_BOOKING_NOT_FOUND = "This Booking object no more exists. Error code can be among missing_journey, missing_booking, missing_user .";
    public static final String COVOITURAGE_PATCH_BOOKING_NOT_FOUND = "The targeted journey, booking or user no more exists. Error code can be among missing_journey, missing_booking, missing_user .";
    public static final String COVOITURAGE_MESSAGE_NOT_FOUND = "The targeted journey or user no more exists.";
    public static final String COVOITURAGE_CONFLICT = "Conflict. This booking has already the new status requested. Error code is status_already_set.";
    public static final String COVOITURAGE_MANY_REQUESTS = "Too Many Requests. Please slow down.";
    public static final String COVOITURAGE_INTERNAL_SERVER_ERROR = "Internal Server Error. Please try again later.";
    public static final String COVOITURAGE_HEADERS_DESCRIPTION = "How long to wait before making a new request (in seconds).";
    public static final String COVOITURAGE_STATUS_RESPONSE_OK = "Ok. Webservice is available.";


    public static final String STANDARD_HEADER_DESCRIPTION = "Standard for the output error";
    
    //Enums parameters
    public static final String BAD_ENUM_VALUE = "{0} is not a valid {1} value, must be one of [{2}]";
}
