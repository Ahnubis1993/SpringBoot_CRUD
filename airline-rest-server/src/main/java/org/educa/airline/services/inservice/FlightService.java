package org.educa.airline.services.inservice;

import org.educa.airline.entity.Flight;
import org.educa.airline.repository.inmemory.InMemoryFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlightService {

    /**
     * Clase a inyectar en el constructor.
     */
    private final InMemoryFlightRepository flightRepository;

    /**
     * Se inyecta la clase InMemoryFlightRepository para hacer uso del mismo.
     *
     * @param flightRepo RepositorioFlight.
     */
    @Autowired
    public FlightService(InMemoryFlightRepository flightRepo) {
        this.flightRepository = flightRepo;
    }

    /**
     * Busca un vuelo por idCode y fecha en el repositorio de vuelos.
     *
     * @param flightIdCode idCode único.
     * @param date         fecha del vuelo.
     * @return el vuelo o null.
     */
    public Flight getFlightByIdCodeDate(String flightIdCode, Date date) {
        return this.flightRepository.getFlightByIdCodeDate(flightIdCode, date);
    }

    /**
     * Busca los vuelos o vuelo que coincidan con el origen y destino.
     *
     * @param origin      origen del vuelo.
     * @param destination destino del vuelo.
     * @return los vuelos, vuelo o null.
     */
    public List<Flight> list(String origin, String destination) {
        return this.flightRepository.list(origin, destination);
    }

    /**
     * Añade un vuelo al repositorio mediante un objeto FlightDTO convertido a FlightEntity.
     *
     * @param flight vuelo a insertar.
     * @return true si ha sido insertado o false si no lo ha sido,
     */
    public boolean addFlight(Flight flight) {
        return this.flightRepository.add(flight);
    }

    /**
     * Borra un vuelo por idCode único.
     *
     * @param flightIdCode idCode del vuelo a borrar.
     * @return true si ha sido borrado o false si no lo ha sido.
     */
    public boolean deleteFlight(String flightIdCode) {
        return this.flightRepository.delete(flightIdCode);
    }

    /**
     * Busca que exista un vuelo por idCode único. Este método se usará en la capa PassengerService
     * para verificar la existencia de un vuelo antes de realizar cualquier operación en un pasajero,
     *
     * @param flightIdCode idCode del vuelo a buscar.
     * @return vuelo encontrado o null.
     */
    public Flight getFlightIdCode(String flightIdCode) {
        return this.flightRepository.getFlightIdCode(flightIdCode);
    }

    /**
     * Buscar el vuelo por idCode en el repositorio de vuelos para ver si existe, en caso de que exista,
     * el vuelo se modifica con los nuevos datos.
     *
     * @param idCode idCode único a buscar del vuelo.
     * @param flight datos del nuevo vuelo.
     * @return true si lo ha encontrado y modificado y false si no lo ha encontrado.
     */
    public boolean updateFlight(String idCode, Flight flight) {
        return this.flightRepository.updateFlight(idCode, flight);
    }

}
