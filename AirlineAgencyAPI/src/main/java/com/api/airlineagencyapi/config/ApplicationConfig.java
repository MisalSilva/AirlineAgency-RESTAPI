/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.config;

/**
 *
 * @author misal
 */
import com.api.airlineagencyapi.resource.BookingResource;
import com.api.airlineagencyapi.resource.FlightResource;
import com.api.airlineagencyapi.resource.PassengerResource;
import com.api.airlineagencyapi.exception.mapper.BadRequestExceptionMapper;
import com.api.airlineagencyapi.exception.mapper.ResourceNotFoundExceptionMapper;
import com.api.airlineagencyapi.exception.mapper.GenericExceptionMapper;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api") // Base URI path: http://localhost:8080/AirlineAgencyAPI/api/
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        // Register resource classes
        resources.add(FlightResource.class);
        resources.add(PassengerResource.class);
        resources.add(BookingResource.class);
        resources.add(BadRequestExceptionMapper.class);
        resources.add(ResourceNotFoundExceptionMapper.class);
        resources.add(GenericExceptionMapper.class);

        return resources;
    }
}
