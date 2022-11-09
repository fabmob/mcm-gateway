package com.gateway.api.service.bookingservice;

import com.gateway.commonapi.dto.api.Booking;
import com.gateway.commonapi.dto.api.CarpoolBookingEvent;
import com.gateway.commonapi.utils.enums.BookingStatus;

import java.util.UUID;

public interface BookingService {

    /**
     * Post a carpooling punctual booking
     * @param partnerId
     * @param booking
     * @return
     */
    Booking postBooking(UUID partnerId, Booking booking);

    /**
     * Update status of an existing booking
     * @param partnerId
     * @param bookingId
     */
    void patchBooking(UUID partnerId, UUID bookingId, BookingStatus status, String message);

    /**
     * Get an existing booking
     * @param partnerId
     * @param bookingId
     * @return
     */
    Booking getBooking(UUID partnerId, UUID bookingId);

    /**
     * Sends booking information of a user connected with a third-party provider back to the provider.
     *
     * @param partnerId partner unique identifier
     * @param carpoolBookingEvent contains all information about the trip, the user (Passenger or Driver) and eventually the Price and the Car if it's a Driver
     */
    void postBookingEvents(UUID partnerId, CarpoolBookingEvent carpoolBookingEvent);
}
