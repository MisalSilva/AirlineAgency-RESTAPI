/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;

import com.api.airlineagencyapi.exception.*;
import com.api.airlineagencyapi.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    private static final Logger logger = LoggerFactory.getLogger(FlightResource.class);
    public static final Map<String, Flight> flightMap = new HashMap<>();

    // POST /flights
    @POST
    public Response createFlight(Flight flight) {
        logger.info("Creating flight: {}", flight.getFlightNumber());

        if (flightMap.containsKey(flight.getFlightNumber())) {
            logger.warn("Flight {} already exists", flight.getFlightNumber());
            throw new FlightAlreadyExistsException(flight.getFlightNumber());
        }

        if (flight.getCapacity() <= 0) {
            logger.warn("Invalid capacity for flight {}: {}", flight.getFlightNumber(), flight.getCapacity());
            throw new InvalidFlightCapacityException(flight.getCapacity());
        }

        flight.setAvailableSeats(flight.getCapacity());
        flightMap.put(flight.getFlightNumber(), flight);

        logger.info("Flight {} created successfully", flight.getFlightNumber());
        return Response.status(Response.Status.CREATED).entity(flight).build();
    }

    // GET /flights
    @GET
    public Response getAllFlights() {
        logger.debug("Retrieving all flights");
        List<Flight> flights = new ArrayList<>(flightMap.values());
        logger.info("Retrieved {} flights", flights.size());
        return Response.ok(flights).build();
    }

    // GET /flights/{flightNumber}
    @GET
    @Path("/{flightNumber}")
    public Response getFlight(@PathParam("flightNumber") String flightNumber) {
        logger.debug("Fetching flight: {}", flightNumber);

        Flight flight = flightMap.get(flightNumber);
        if (flight == null) {
            logger.warn("Flight {} not found", flightNumber);
            throw new FlightNotFoundException(flightNumber);
        }

        logger.info("Flight {} retrieved successfully", flightNumber);
        return Response.ok(flight).build();
    }

    // PUT /flights/{flightNumber}
    @PUT
    @Path("/{flightNumber}")
    public Response updateFlight(@PathParam("flightNumber") String flightNumber, Flight updated) {
        logger.info("Updating flight: {}", flightNumber);

        Flight existing = flightMap.get(flightNumber);
        if (existing == null) {
            logger.warn("Flight {} not found for update", flightNumber);
            throw new FlightNotFoundException(flightNumber);
        }

        existing.setOrigin(updated.getOrigin());
        existing.setDestination(updated.getDestination());
        existing.setDepartureTime(updated.getDepartureTime());
        existing.setArrivalTime(updated.getArrivalTime());
        existing.setCapacity(updated.getCapacity());
        existing.setAvailableSeats(updated.getAvailableSeats());

        logger.info("Flight {} updated successfully", flightNumber);
        return Response.ok(existing).build();
    }

    // DELETE /flights/{flightNumber}
    @DELETE
    @Path("/{flightNumber}")
    public Response deleteFlight(@PathParam("flightNumber") String flightNumber) {
        logger.info("Deleting flight: {}", flightNumber);

        Flight removed = flightMap.remove(flightNumber);
        if (removed == null) {
            logger.warn("Flight {} not found for deletion", flightNumber);
            throw new FlightNotFoundException(flightNumber);
        }

        logger.info("Flight {} deleted successfully", flightNumber);
        return Response.ok(Map.of("message", "Flight deleted")).build();
    }
}
