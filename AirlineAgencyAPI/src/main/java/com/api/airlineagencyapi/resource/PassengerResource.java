/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.resource;

/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.model.Passenger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerResource {

    private static final Map<String, Passenger> passengerMap = new HashMap<>();
    private static int passengerIdCounter = 1;

    // POST /passengers
    @POST
    public Response createPassenger(Passenger passenger) {
        String id = String.valueOf(passengerIdCounter++);
        passenger.setPassengerId(id);
        passengerMap.put(id, passenger);
        return Response.status(Response.Status.CREATED).entity(passenger).build();
    }

    // GET /passengers
    @GET
    public Response getAllPassengers() {
        return Response.ok(new ArrayList<>(passengerMap.values())).build();
    }

    // GET /passengers/{id}
    @GET
    @Path("/{id}")
    public Response getPassenger(@PathParam("id") String id) {
        Passenger passenger = passengerMap.get(id);
        if (passenger == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Passenger not found"))
                    .build();
        }
        return Response.ok(passenger).build();
    }

    // PUT /passengers/{id}
    @PUT
    @Path("/{id}")
    public Response updatePassenger(@PathParam("id") String id, Passenger updated) {
        Passenger existing = passengerMap.get(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Passenger not found"))
                    .build();
        }

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        return Response.ok(existing).build();
    }

    // DELETE /passengers/{id}
    @DELETE
    @Path("/{id}")
    public Response deletePassenger(@PathParam("id") String id) {
        Passenger removed = passengerMap.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Passenger not found"))
                    .build();
        }
        return Response.ok(Map.of("message", "Passenger deleted")).build();
    }
}
