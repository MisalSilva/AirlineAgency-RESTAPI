/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;

/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.model.Booking;
import com.api.airlineagencyapi.model.Flight;
import com.api.airlineagencyapi.model.Passenger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private static final Map<String, Booking> bookingMap = new HashMap<>();
    private static int bookingCounter = 1;

    // Reuse the in-memory data from other resources
    private static final Map<String, Flight> flightMap = FlightResource.flightMap;
    private static final Map<String, Passenger> passengerMap = PassengerResource.passengerMap;

    // POST /bookings
    @POST
    public Response createBooking(Booking booking) {
        Flight flight = flightMap.get(booking.getFlightNumber());
        Passenger passenger = passengerMap.get(booking.getPassengerId());

        if (flight == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Flight not found"))
                    .build();
        }

        if (passenger == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Passenger not found"))
                    .build();
        }

        if (flight.getAvailableSeats() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "No available seats on flight"))
                    .build();
        }

        String id = String.valueOf(bookingCounter++);
        booking.setBookingId(id);
        booking.setSeatNumber("AUTO"); // You can generate real seat numbers if needed
        bookingMap.put(id, booking);

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    // GET /bookings
    @GET
    public Response getAllBookings() {
        return Response.ok(new ArrayList<>(bookingMap.values())).build();
    }

    // GET /bookings/{id}
    @GET
    @Path("/{id}")
    public Response getBooking(@PathParam("id") String id) {
        Booking booking = bookingMap.get(id);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Booking not found"))
                    .build();
        }
        return Response.ok(booking).build();
    }

    // DELETE /bookings/{id}
    @DELETE
    @Path("/{id}")
    public Response cancelBooking(@PathParam("id") String id) {
        Booking booking = bookingMap.remove(id);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Booking not found"))
                    .build();
        }

        // Increment flight seat
        Flight flight = flightMap.get(booking.getFlightNumber());
        if (flight != null) {
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        }

        return Response.ok(Map.of("message", "Booking canceled")).build();
    }
}
