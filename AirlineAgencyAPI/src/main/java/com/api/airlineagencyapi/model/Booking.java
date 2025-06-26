/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.airlineagencyapi.model;

/**
 *
 * @author misal
 */
public class Booking {
    private String bookingId;
    private String flightNumber;
    private String passengerId;
    private String bookingDate;  // ISO 8601 format (e.g., "2025-10-01T15:00:00Z")
    private String seatNumber;   // Optional

    public Booking() {}

    public Booking(String bookingId, String flightNumber, String passengerId, String bookingDate, String seatNumber) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.passengerId = passengerId;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
