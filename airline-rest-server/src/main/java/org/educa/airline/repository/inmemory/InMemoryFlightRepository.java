package org.educa.airline.repository.inmemory;

import org.educa.airline.entity.Flight;
import org.educa.airline.repository.FlightRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryFlightRepository implements FlightRepository {
    private final Map<String, Flight> flights = new HashMap<>();

    /**
     * Busca vuelos mediante una ciudad de origen y una de llegada.
     *
     * @param origin      dato a buscar en los vuelos.
     * @param destination dato s buscar en los vuelos.
     * @return una lista de vuelos.
     */
    @Override
    public List<Flight> list(String origin, String destination) {
        return flights
                .values()
                .stream()
                .filter(f -> origin == null || origin.equals(f.getOrigin()))
                .filter(f -> destination == null || destination.equals(f.getDestination()))
                .collect(Collectors.toList());


    }

    /**
     * Busca un vuelo mediante id (clave del Map) y fecha del mismo.
     *
     * @param id   clave a buscar en los vuelos.
     * @param date dato a buscar en el vuelo.
     * @return un vuelo si lo ha encontrado o null si no lo ha encontrado.
     */
    public Flight getFlightByIdCodeDate(String id, Date date) {
        // Puede haber vuelos con diferente idCode y mismo id y fecha, pero no tendría sentido,
        // ya que sería repetido. Puede haber otro vuelo igual pero en diferente fecha.
        for (Flight flight : flights.values()) {
            // Comprobar si el id coincide con el id del vuelo
            if (flight.getId().equals(id) && flight.getDate().equals(date)) {
                // Sí coincide, devolver el vuelo
                return flight;
            }
        }
        return null;
    }

    /**
     * Busca un vuelo por clave única del HashMap.
     * Este método será usado por ServicePassenger, para verificar la existencia del vuelo.
     *
     * @param flightIdCode clave a buscar en el HashMap.
     * @return un vuelo si lo ha encontrado o null si no lo ha encontrado.
     */
    @Override
    public Flight getFlightIdCode(String flightIdCode) {
        return flights.get(flightIdCode);
    }

    @Override
    public Flight getFlight(String flightId) {
        return flights.get(flightId);
    }

    /**
     * Añade un vuelo al HashMap.
     *
     * @param flight vuelo a insertar.
     * @return true si se ha insertado, false si no se ha insertado.
     */
    @Override
    public synchronized boolean add(Flight flight) {
        if (!flights.containsKey(flight.getIdCode())) {
            flights.put(flight.getIdCode(), flight);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean updateFlight(String flightIdCode, Flight flight) {
        // 3 -> Madrid - Londres
        // 4 -> 1 Madrid - Berlin
        if (flights.containsKey(flightIdCode)) {
            flights.remove(flightIdCode);
            flights.put(flight.getIdCode(), flight);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Borra un vuelo por clave.
     *
     * @param flightIdCode clave del vuelo a buscar en el HashMap.
     * @return true si se ha borrado, false si no se ha borrado.
     */
    @Override
    public synchronized boolean delete(String flightIdCode) {
        if (flights.containsKey(flightIdCode)) {
            flights.remove(flightIdCode);
            return true;
        } else {
            return false;
        }
    }

}
