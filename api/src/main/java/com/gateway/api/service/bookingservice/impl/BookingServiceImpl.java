package com.gateway.api.service.bookingservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.service.bookingservice.BookingService;
import com.gateway.api.service.ivservice.impl.IVServiceImpl;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.dto.api.Booking;
import com.gateway.commonapi.dto.api.CarpoolBookingEvent;
import com.gateway.commonapi.dto.api.DriverCarpoolBooking;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gateway.api.util.constant.GatewayMessageDict.*;
import static com.gateway.commonapi.constants.ErrorCodeDict.*;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;
import static com.gateway.commonapi.utils.enums.ActionsEnum.*;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ErrorMessages errorMessages;

    @Autowired
    IVServiceImpl ivService;

    @Autowired
    ValidityUtils validityUtils;

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String SEPARATOR = ": ";

    /**
     * check booking params for post bookingEvents and postBooking
     */
    private void checkBookingParams(Integer driverGrade, Integer passengerGrade, Double passengerDropLng, Double passengerPickupLng,
                                    Double passengerPickupLat, Double passengerDropLat, Integer duration, Integer distance, Float amount) {
        List<String> messages = new ArrayList<>();
        List<Integer> errorCodes = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        ValidityUtils.checkGrade(messages, titles, errorCodes, driverGrade);
        ValidityUtils.checkGrade(messages, titles, errorCodes, passengerGrade);

        ValidityUtils.checkLongitude(messages, titles, errorCodes, passengerDropLng);
        ValidityUtils.checkLongitude(messages, titles, errorCodes, passengerPickupLng);
        ValidityUtils.checkLatitude(messages, titles, errorCodes, passengerPickupLat);
        ValidityUtils.checkLatitude(messages, titles, errorCodes, passengerDropLat);

        if (duration != null) {
            ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, Float.valueOf(duration), INVALID_DURATION_COVOIT, null, DURATION_CODE);
        }
        if (distance != null) {
            ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, Float.valueOf(distance), INVALID_DISTANCE_COVOIT, null, DISTANCE_CODE);
        }
        ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, amount, INVALID_AMOUNT_COVOIT, null, AMOUNT_CODE);

        if (!messages.isEmpty()) {
            String globalMessage = StringUtils.join(messages, " ");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            GenericError genericError = new GenericError();
            genericError.setErrorCode(errorCodes.get(0));
            genericError.setTimestamp(simpleDateFormat.format(new Date()));
            genericError.setDescription(globalMessage);

            throw new BadRequestException(genericError);
        }
    }

    @Override
    public Booking postBooking(UUID partnerId, Booking booking) {
        validityUtils.checkPartnerId(partnerId);
        this.checkBookingParams(booking.getDriver() != null ? booking.getDriver().getGrade() : null, booking.getPassenger() != null ? booking.getPassenger().getGrade() : null, booking.getPassengerDropLng(), booking.getPassengerPickupLng(), booking.getPassengerPickupLat(), booking.getPassengerDropLat(), booking.getDuration(), booking.getDistance(), booking.getPrice().getAmount());

        Booking bookingCreated = new Booking();
        Map<String, Object> body = objectMapper.convertValue(booking, new TypeReference<Map<String, Object>>() {
        });
        List<Object> response = (List<Object>) this.getRouting(partnerId, CARPOOLING_BOOK.value, Optional.of(body), null);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(POST_CARPOOLING_BOOKING, PARTNER_ID, String.valueOf(partnerId))));
        }

        try {

            List<Booking> list = objectMapper.convertValue(response, new TypeReference<List<Booking>>() {
            });
            bookingCreated = list.get(0);

        } catch (Exception e) {
            this.exceptionHandler(e, POST_CARPOOLING_BOOKING, partnerId);
        }

        return bookingCreated;

    }

    @Override
    public void patchBooking(UUID partnerId, UUID bookingId, BookingStatus status, String message) {
        validityUtils.checkPartnerId(partnerId);

        Map<String, String> params = new HashMap<>();
        if (bookingId != null) {
            params.put(BOOKING_ID, String.valueOf(bookingId));
        }
        if (StringUtils.isNotBlank(status.getValue())) {
            params.put(STATUS, String.valueOf(status));
        }
        if (StringUtils.isNotBlank(message)) {
            params.put(MESSAGE, message);
        }

        this.getRouting(partnerId, CARPOOLING_PATCH_BOOKING.value, Optional.empty(), params);


    }

    @Override
    public Booking getBooking(UUID partnerId, UUID bookingId) {
        validityUtils.checkPartnerId(partnerId);

        Map<String, String> params = new HashMap<>();
        Booking bookingGet = new Booking();

        if (bookingId != null) {
            params.put(BOOKING_ID, String.valueOf(bookingId));
        }

        List<Object> response = (List<Object>) this.getRouting(partnerId, CARPOOLING_BOOKING_SEARCH.value, Optional.empty(), params);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_CARPOOLING_BOOKING, PARTNER_ID, String.valueOf(partnerId), BOOKING_ID, String.valueOf(bookingId))));
        }

        try {

            List<Booking> list = objectMapper.convertValue(response, new TypeReference<List<Booking>>() {
            });
            bookingGet = list.get(0);

        } catch (Exception e) {
            this.exceptionHandler(e, GET_CARPOOLING_BOOKING, partnerId);
        }

        if (bookingGet.getId() != null && !bookingGet.getId().equals(bookingId)) {
            GenericError genericError = new GenericError();
            genericError.setErrorCode(BOOKING_ID_CODE);
            String msg = CommonUtils.placeholderFormat(UNKNOWN_BOOKING_ID, FIRST_PLACEHOLDER, bookingId != null ? bookingId.toString() : null);
            genericError.setDescription(msg);
            throw new BadRequestException(genericError);
        }

        return bookingGet;
    }


    @Override
    public void postBookingEvents(UUID partnerId, CarpoolBookingEvent carpoolBookingEvent) {
        validityUtils.checkPartnerId(partnerId);

        DriverCarpoolBooking driverCarpoolBooking = (DriverCarpoolBooking) carpoolBookingEvent.getData();

        this.checkBookingParams(driverCarpoolBooking.getDriver().getGrade(), null, driverCarpoolBooking.getPassengerDropLng(), driverCarpoolBooking.getPassengerPickupLng(), driverCarpoolBooking.getPassengerPickupLat(), driverCarpoolBooking.getPassengerDropLat(), driverCarpoolBooking.getDuration() != null ? driverCarpoolBooking.getDuration().intValue() : null, driverCarpoolBooking.getDistance() != null ? driverCarpoolBooking.getDistance().intValue() : null, driverCarpoolBooking.getPrice() != null ? driverCarpoolBooking.getPrice().getAmount() : null);

        Map<String, Object> body = objectMapper.convertValue(carpoolBookingEvent, new TypeReference<Map<String, Object>>() {
        });
        this.getRouting(partnerId, BOOKING_EVENT.value, Optional.of(body), null);


    }


    /**
     * Call routing
     *
     * @param partnerId  partner unique identifier
     * @param actionName name of action
     * @param body       body
     * @param params     parameters
     * @return response
     */
    private Object getRouting(UUID partnerId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {

        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();

        String mspMetaIdValue = partnerId != null ? partnerId.toString() : null;
        Object partnerBusinessResponse = null;
        String urlCall = routingApiUri + CommonUtils.placeholderFormat(GET_PARTNER_ID_PATH, PARTNER_ID, mspMetaIdValue
                + GET_ACTION_NAME_PATH, ACTION_NAME, actionName);
        String urlTemplate = CommonUtils.constructUrlTemplate(urlCall, params);
        HttpEntity<Optional<Map<String, Object>>> entity = new HttpEntity<>(body, CommonUtils.setHeaders().getHeaders());
        log.debug(LOG_URL_CALL, urlTemplate);

        boolean preserveOriginalErrors = false;
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        if (StringUtils.isNotBlank(outputStandard)) {
            preserveOriginalErrors = CommonUtils.shouldPreserveResponseStatus(outputStandard);
        }

        try {
            ResponseEntity<Object> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity, Object.class);
            if (response.getBody() != null) {
                partnerBusinessResponse = Objects.requireNonNull(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (HttpServerErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new InternalException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        }

        return partnerBusinessResponse;
    }

    /**
     * Return a customed exception with initial error message if it exists or a customed global one
     *
     * @param e                exception name
     * @param serviceCalledMsg Message
     * @param partnerId        partner unique identifier
     */
    private void exceptionHandler(Exception e, String serviceCalledMsg, UUID partnerId) {
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();

        if (e.getMessage() != null) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(serviceCalledMsg, PARTNER_ID, String.valueOf(partnerId))));
        }
    }
}
