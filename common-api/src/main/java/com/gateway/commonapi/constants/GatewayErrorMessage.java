package com.gateway.commonapi.constants;

public class GatewayErrorMessage {
    private GatewayErrorMessage() {
    }

    // lat
    public static final String INVALID_LAT_MESSAGE = "Invalid latitude {0}. Latitude must be between -90 and +90.";
    public static final String INVALID_TYPE = "Invalid";
    public static final String INVALID_LAT_TITLE = "Invalid latitude";

    // lon
    public static final String INVALID_LON_MESSAGE = "Invalid longitude {0}. Longitude must be between -180 and +180.";
    public static final String INVALID_LON_TITLE = "Invalid longitude";

    // rad
    public static final String INVALID_RAD_MESSAGE = "Invalid radius {0}. Radius must be positive or zero.";
    public static final String INVALID_RAD_TITLE = "Invalid radius";

    // maxResult
    public static final String INVALID_MAX_RESULT = "Invalid max result {0}. Max result must be positive and not zero.";
    public static final String INVALID_MAX_RESULT_TITLE = "Invalid max result";

    // partnerId
    public static final String UNKNOWN_PARTNER_ID_MESSAGE = "Unknown partner id {0}.";
    public static final String UNKNOWN_PARTNER_ID = "Unknown partner id";
    public static final String INVALID_PARTNER_ID = "Invalid UUID";
    public static final String UNKNOWN_AREA_TYPE = "Unknown area type";
    public static final String UNKNOWN_PARTNER_TYPE = "Unknown partner type";
    public static final String INVALID_FORMAT_UUID = "Invalid UUID";


    public static final String UNKNOWN_AREA_MESSAGE = "Unknown area type {0}. Allowed area types are 'OPERATING', 'NO_PARKING', 'SPEED_LIMIT', or 'PREFERENTIAL_PARKING'.";
    public static final String UNKNOWN_PARTNER_TYPE_MESSAGE = "Unknown partner type {0}. Allowed partner types are 'MSP' or 'MAAS'.";
    public static final String INVALID_PARTNER_ID_MESSAGE = "Invalid UUID {0}. Unauthorized to reach this partner.";


    public static final String PLACEHOLDER = "0";

    // COVOIT IV
    public static final String INVALID_RAD_MESSAGE_COVOIT = "Invalid radius {0}. Radius must be positive or zero.";
    public static final String INVALID_MAX_RESULT_COVOIT = "Invalid max result {0}. Invalid max result be positive or zero.";
    public static final String INVALID_TIME_OF_DAY_COVOIT = "Invalid time of day {0}. Format of time of day must be HH:MM:SS.";
    public static final String INVALID_DAY_OF_WEEK_COVOIT = "Invalid day of week {0}. Day of week must be at least one of MON,TUE,WED,THU,FRI,SAT,SUN.";
    public static final String INVALID_MIN_OR_MAX_DEPARTURE_DATE_COVOIT = "Invalid min or max departure date {0}, {1}. Minimal departure date must be less than maximal departure date.";

    // COVOIT MSG
    public static final String INVALID_GRADE_COVOIT = "Invalid grade {0}. Grade must be from 1 to 5.";
    public static final String INVALID_GENDER_COVOIT = "Invalid gender {0}. Gender must be one of F, M or O.";
    public static final String INVALID_STATUS_COVOIT = "Invalid status {0}. Status must be one of WAITING_CONFIRMATION, CONFIRMED, CANCELLED, COMPLETED_PENDING_VALIDATION or VALIDATED.";
    public static final String INVALID_DISTANCE_COVOIT = "Invalid distance {0}. Distance must be positive and not zero.";
    public static final String INVALID_DURATION_COVOIT = "Invalid duration {0}. Duration must be positive and not zero.";
    public static final String INVALID_TYPE_OF_PRICE_COVOIT = "Invalid type of price {0}. Type of price must be one of FREE, PAYING or UNKNOWN.";
    public static final String INVALID_AMOUNT_COVOIT = "Invalid amount of price {0}. Amount of price must be positive or zero.";
    public static final String INVALID_TYPE_OF_RECIPIENT_COVOIT = "Invalid recipient carpooler type {0}. Recipient carpooler type must be one of DRIVER or PASSENGER.";
    public static final String UNKNOWN_BOOKING_ID = "Unknown booking id {0}.";

    // Internal error messages tomp
    public static final String INTERNAL_ERROR_DETAIL = "Internal server error";
    public static final String DEFAULT_TOMP_TYPE = "Technical issue";
    public static final String DEFAULT_TOMP_INSTANCE = "Gateway callId ";
    public static final String UNAUTHORIZED_TITLE = "Unauthorized";
    public static final String FORBIDDEN_TITLE = "Forbidden";
    public static final String FORBIDDEN_TYPE_TITLE = "Forbidden type of partner";

    public static final String FORBIDDEN_TYPE_MESSAGE = "Forbidden type of partner [{0}]. You are not allowed to search this type of partner.";
    public static final String ILLEGAL_OPERATION = "Illegal Operation";
    public static final String DETAIL_MESSAGE_AUTHENTICATION = "The request requires user authentication.";
    public static final String DETAIL_MESSAGE_FORBIDDEN = "The server understood the request, but is refusing to fulfill it.";
    public static final String TITLE_FORMAT = "Gateway error : {0}";
    public static final String DETAIL_FORMAT = "Gateway error happen : {0}, contact your Gateway support.";
    public static final String INTERNAL_ERROR_TITLE = "Internal error";

    // Internal error messages covoiturage
    public static final String INTERNAL_MESSAGE_ERROR_CARPOOLING = "Internal server error, please contact your administrator or try again latter.";
    public static final String UNEXPECTED_FORMAT = "Unexpected format, please check the body of the request.";
    public static final String INTERNAL_MESSAGE_ERROR = "Internal server error, please contact your administrator or try again later.";
    public static final String UUID_PARSE_ERROR = "java.util.UUID";
    public static final String INTERNAL_SERVER_ERROR_LABEL = "Internal Server Error";

}

