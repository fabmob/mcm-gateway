package com.gateway.api.service.bookingserviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.service.bookingservice.impl.BookingServiceImpl;
import com.gateway.api.service.ivservice.impl.IVServiceImpl;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.dto.api.Booking;
import com.gateway.commonapi.dto.api.CarpoolBookingEvent;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.enums.BookingStatus;
import com.gateway.commonapi.utils.enums.StandardEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String GATEWAY_API_BOOKING_JSON = "gateway-api/request/postBookingBody.json";
    public static final String GATEWAY_API_CARPOOL_BOOKING_EVENT_JSON = "gateway-api/request/postBookingEvent.json";
    public static final String GATEWAY_API_BOOKING_EXCEPTION_JSON = "gateway-api/request/postBookingExceptionBody.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ValidityUtils validityUtils;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private IVServiceImpl ivService;

    @Mock
    private ErrorMessages errorMessages;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }


    private List<Object> createMockBooking() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_BOOKING_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<>() {
        });
        Object response = objectReader.readValue(expectedStringyfied);
        List<Object> expectedResponse = new ArrayList<>();
        expectedResponse.add(response);
        return expectedResponse;

    }

    private Booking createMockBookingObject() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_BOOKING_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Booking>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private Booking createMockBookingObjectException() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_BOOKING_EXCEPTION_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Booking>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private CarpoolBookingEvent createMockCarpoolBookingEventObject() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_CARPOOL_BOOKING_EVENT_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<CarpoolBookingEvent>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockCarpoolBookingEvent() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_CARPOOL_BOOKING_EVENT_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<>() {
        });
        Object response = objectReader.readValue(expectedStringyfied);
        List<Object> expectedResponse = new ArrayList<>();
        expectedResponse.add(response);
        return expectedResponse;

    }

    @Test
    void testGetBooking() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockBooking());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);


        Booking booking = bookingService.getBooking(partnerId, bookingId);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", booking.getId().toString());

        UUID wrongBookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa5");
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(partnerId, wrongBookingId));
    }


    @Test
    void testGetBookingNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(partnerId, bookingId));
    }

    @Test
    void testGetBookingException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> bookingService.getBooking(partnerId, bookingId));
    }


    @Test
    void testGetBookingExceptionFromAdapter() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(partnerId, bookingId));


    }

    @Test
    void testGetBookingFromAdapter2() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(InternalException.class, () -> bookingService.getBooking(partnerId, bookingId));

    }

    @Test
    void testGetBookingRestRequestException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(BadGatewayException.class, () -> bookingService.getBooking(partnerId, bookingId));


    }

    @Test
    void testGetBookingExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertThrows(NullPointerException.class, () -> bookingService.getBooking(partnerId, bookingId));
    }


    @Test
    void testGetBookingExceptionFromAdapterTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpClientErrorException.class, () -> bookingService.getBooking(partnerId, bookingId));


    }

    @Test
    void testGetBookingFromAdapter2TOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpServerErrorException.class, () -> bookingService.getBooking(partnerId, bookingId));

    }

    @Test
    void testGetBookingRestRequestExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(RestClientException.class, () -> bookingService.getBooking(partnerId, bookingId));


    }

    @Test
    void testPostBooking() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockBooking());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        Booking booking = bookingService.postBooking(partnerId, this.createMockBookingObject());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", booking.getId().toString());
    }

    @Test
    void testPostBookingExceptionParams() throws IOException {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.COVOITURAGE_STANDARD);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        Booking booking = this.createMockBookingObjectException();
        assertThrows(BadRequestException.class, () -> bookingService.postBooking(partnerId, booking));
    }


    @Test
    void testPostBookingNull() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        Booking booking = this.createMockBookingObject();
        assertThrows(NotFoundException.class, () -> bookingService.postBooking(partnerId, booking));
    }

    @Test
    void testPostBookingException() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        Booking booking = this.createMockBookingObject();
        assertThrows(UnavailableException.class, () -> bookingService.postBooking(mspId, booking));
    }


    @Test
    void testPostBookingExceptionFromAdapter() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        doNothing().when(validityUtils).checkPartnerId(partnerId);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Booking booking = this.createMockBookingObject();
        assertThrows(NotFoundException.class, () -> bookingService.postBooking(partnerId, booking));


    }


    @Test
    void testPostBookingRestRequestException() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Booking booking = this.createMockBookingObject();
        assertThrows(BadGatewayException.class, () -> bookingService.postBooking(partnerId, booking));


    }


    @Test
    void testPatchBooking() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        UUID bookingId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        ResponseEntity response = ResponseEntity.ok().build();
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        bookingService.patchBooking(partnerId, bookingId, BookingStatus.CONFIRMED, "test");
        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));

    }

    @Test
    void testPostBookingEvent() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockCarpoolBookingEvent());

        ObjectMapper mapper = new ObjectMapper();
        mapper.convertValue(this.createMockCarpoolBookingEventObject(), new TypeReference<Map<String, Object>>() {
        });
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        bookingService.postBookingEvents(partnerId, this.createMockCarpoolBookingEventObject());
        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));
    }

    @Test
    void testPostBookingEventException() throws IOException {
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        CarpoolBookingEvent carpoolBookingEvent = this.createMockCarpoolBookingEventObject();
        assertThrows(BadGatewayException.class, () -> bookingService.postBookingEvents(null, carpoolBookingEvent));
    }
}
