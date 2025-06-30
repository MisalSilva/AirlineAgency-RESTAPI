/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;

/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.exception.*;
import com.api.airlineagencyapi.model.Booking;
import com.api.airlineagencyapi.model.Flight;
import com.api.airlineagencyapi.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private static final Logger logger = LoggerFactory.getLogger(BookingResource.class);

    private static final Map<String, Booking> bookingMap = new HashMap<>();
    private static int bookingCounter = 1;

    // Reuse shared maps from other resources
    public static final Map<String, Flight> flightMap = FlightResource.flightMap;
    public static final Map<String, Passenger> passengerMap = PassengerResource.passengerMap;

    // POST /bookings
    @POST
    public Response createBooking(Booking booking) {
        logger.info("Creating booking for flight {} and passenger {}", booking.getFlightNumber(), booking.getPassengerId());

        Flight flight = flightMap.get(booking.getFlightNumber());
        if (flight == null) {
            logger.warn("Flight {} not found", booking.getFlightNumber());
            throw new FlightNotFoundException(booking.getFlightNumber());
        }

        Passenger passenger = passengerMap.get(booking.getPassengerId());
        if (passenger == null) {
            logger.warn("Passenger {} not found", booking.getPassengerId());
            throw new PassengerNotFoundException(booking.getPassengerId());
        }

        if (flight.getAvailableSeats() <= 0) {
            logger.warn("No available seats on flight {}", flight.getFlightNumber());
            throw new NoAvailableSeatsException(flight.getFlightNumber());
        }

        String bookingId = String.valueOf(bookingCounter++);
        booking.setBookingId(bookingId);
        booking.setSeatNumber("AUTO"); // Optional: generate seat numbers realistically
        booking.setBookingDate(java.time.ZonedDateTime.now().toString());

        bookingMap.put(bookingId, booking);
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);

        logger.info("Booking {} created successfully", bookingId);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    // GET /bookings
    @GET
    public Response getAllBookings() {
        logger.debug("Fetching all bookings");
        List<Booking> bookings = new ArrayList<>(bookingMap.values());
        logger.info("Retrieved {} bookings", bookings.size());
        return Response.ok(bookings).build();
    }

    // GET /bookings/{id}
    @GET
    @Path("/{id}")
    public Response getBooking(@PathParam("id") String id) {
        logger.debug("Fetching booking ID {}", id);
        Booking booking = bookingMap.get(id);

        if (booking == null) {
            logger.warn("Booking {} not found", id);
            throw new BookingNotFoundException(id);
        }

        logger.info("Booking {} retrieved successfully", id);
        return Response.ok(booking).build();
    }

    // DELETE /bookings/{id}
    @DELETE
    @Path("/{id}")
    public Response cancelBooking(@PathParam("id") String id) {
        logger.info("Cancelling booking {}", id);
        Booking booking = bookingMap.remove(id);

        if (booking == null) {
            logger.warn("Booking {} not found for cancellation", id);
            throw new BookingNotFoundException(id);
        }

        // Increment seat count for related flight
        Flight flight = flightMap.get(booking.getFlightNumber());
        if (flight != null) {
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            logger.debug("Incremented available seats for flight {}", flight.getFlightNumber());
        }

        logger.info("Booking {} cancelled successfully", id);
        return Response.ok(Map.of("message", "Booking cancelled")).build();
    }
}