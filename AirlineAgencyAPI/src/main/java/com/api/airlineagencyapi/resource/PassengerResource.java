/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;

/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.exception.PassengerNotFoundException;
import com.api.airlineagencyapi.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerResource {

    private static final Logger logger = LoggerFactory.getLogger(PassengerResource.class);
    public static final Map<String, Passenger> passengerMap = new HashMap<>();
    private static int passengerIdCounter = 1;

    // POST /passengers
    @POST
    public Response createPassenger(Passenger passenger) {
        String id = String.valueOf(passengerIdCounter++);
        passenger.setPassengerId(id);
        passengerMap.put(id, passenger);

        logger.info("Created passenger with ID: {}", id);
        return Response.status(Response.Status.CREATED).entity(passenger).build();
    }

    // GET /passengers
    @GET
    public Response getAllPassengers() {
        logger.debug("Fetching all passengers");
        List<Passenger> passengers = new ArrayList<>(passengerMap.values());
        logger.info("Retrieved {} passengers", passengers.size());
        return Response.ok(passengers).build();
    }

    // GET /passengers/{id}
    @GET
    @Path("/{id}")
    public Response getPassenger(@PathParam("id") String id) {
        logger.debug("Fetching passenger with ID: {}", id);
        Passenger passenger = passengerMap.get(id);

        if (passenger == null) {
            logger.warn("Passenger with ID {} not found", id);
            throw new PassengerNotFoundException(id);
        }

        logger.info("Passenger with ID {} retrieved", id);
        return Response.ok(passenger).build();
    }

    // PUT /passengers/{id}
    @PUT
    @Path("/{id}")
    public Response updatePassenger(@PathParam("id") String id, Passenger updated) {
        logger.info("Updating passenger with ID: {}", id);
        Passenger existing = passengerMap.get(id);

        if (existing == null) {
            logger.warn("Passenger with ID {} not found for update", id);
            throw new PassengerNotFoundException(id);
        }

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());

        logger.info("Passenger with ID {} updated", id);
        return Response.ok(existing).build();
    }

    // DELETE /passengers/{id}
    @DELETE
    @Path("/{id}")
    public Response deletePassenger(@PathParam("id") String id) {
        logger.info("Deleting passenger with ID: {}", id);
        Passenger removed = passengerMap.remove(id);

        if (removed == null) {
            logger.warn("Passenger with ID {} not found for deletion", id);
            throw new PassengerNotFoundException(id);
        }

        logger.info("Passenger with ID {} deleted", id);
        return Response.ok(Map.of("message", "Passenger deleted")).build();
    }
}