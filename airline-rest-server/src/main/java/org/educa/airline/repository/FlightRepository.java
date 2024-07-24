package org.educa.airline.repository;

import org.educa.airline.entity.Flight;

import java.util.Date;
import java.util.List;

public interface FlightRepository {
    List<Flight> list(String origin, String destination);

    Flight getFlightByIdCodeDate(String flightIdCode, Date date);

    Flight getFlightIdCode(String flightIdCode);

    Flight getFlight(String flightId);

    boolean add(Flight flight);

    boolean updateFlight(String flightIdCode, Flight flight);

    boolean delete(String flightIdCode);
}
