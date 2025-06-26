/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;
/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.model.Flight;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    private static final Map<String, Flight> flightMap = new HashMap<>();

    // POST /flights
    @POST
    public Response createFlight(Flight flight) {
        if (flightMap.containsKey(flight.getFlightNumber())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Flight already exists"))
                    .build();
        }

        if (flight.getCapacity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Flight capacity must be greater than 0"))
                    .build();
        }

        flight.setAvailableSeats(flight.getCapacity());
        flightMap.put(flight.getFlightNumber(), flight);
        return Response.status(Response.Status.CREATED).entity(flight).build();
    }

    // GET /flights
    @GET
    public Response getAllFlights() {
        return Response.ok(new ArrayList<>(flightMap.values())).build();
    }

    // GET /flights/{flightNumber}
    @GET
    @Path("/{flightNumber}")
    public Response getFlight(@PathParam("flightNumber") String flightNumber) {
        Flight flight = flightMap.get(flightNumber);
        if (flight == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Flight not found"))
                    .build();
        }
        return Response.ok(flight).build();
    }

    // PUT /flights/{flightNumber}
    @PUT
    @Path("/{flightNumber}")
    public Response updateFlight(@PathParam("flightNumber") String flightNumber, Flight updated) {
        Flight existing = flightMap.get(flightNumber);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Flight not found"))
                    .build();
        }

        existing.setOrigin(updated.getOrigin());
        existing.setDestination(updated.getDestination());
        existing.setDepartureTime(updated.getDepartureTime());
        existing.setArrivalTime(updated.getArrivalTime());
        existing.setCapacity(updated.getCapacity());
        existing.setAvailableSeats(updated.getAvailableSeats());
        return Response.ok(existing).build();
    }

    // DELETE /flights/{flightNumber}
    @DELETE
    @Path("/{flightNumber}")
    public Response deleteFlight(@PathParam("flightNumber") String flightNumber) {
        Flight removed = flightMap.remove(flightNumber);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Flight not found"))
                    .build();
        }
        return Response.ok(Map.of("message", "Flight deleted")).build();
    }
}
