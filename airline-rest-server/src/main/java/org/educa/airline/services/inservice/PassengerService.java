package org.educa.airline.services.inservice;

import org.educa.airline.entity.Flight;
import org.educa.airline.entity.Passenger;
import org.educa.airline.repository.inmemory.InMemoryPassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    /**
     * Clases a inyectar en el constructor.
     */
    private final InMemoryPassengerRepository repositoryPassenger;
    private final FlightService flightService;

    /**
     * Se inyectan ambas clases para hacer uso de las mismas en esta capa.
     *
     * @param repositoryPassenger repositorio de pasajeros.
     * @param flightService       servicio de vuelos.
     */
    @Autowired
    public PassengerService(InMemoryPassengerRepository repositoryPassenger, FlightService flightService) {
        this.repositoryPassenger = repositoryPassenger;
        this.flightService = flightService;
    }

    /**
     * Añade un pasajero.
     *
     * @param passenger pasajero a insertar.
     * @return true si ha sido insertado, false si no ha sido insertado.
     */
    public boolean addPassenger(Passenger passenger) {
        return this.repositoryPassenger.addPassenger(passenger);
    }

    /**
     * Consigue un pasajero del repositorio de pasajeros.
     *
     * @param idCode clave del 1 HashMap.
     * @param nif    clave del 2 HashMap que está dentro del 1.
     * @return true si ha sido insertado, false si no ha sido insertado.
     */
    public Passenger getPassenger(String idCode, String nif) {
        return this.repositoryPassenger.getPassenger(idCode, nif);
    }

    /**
     * Consigue un vuelo por idCode (clave única del HashMap del repositorio vuelos)
     * Este método corresponde a la capa del servicio de vuelos, pero será necesario para realizar
     * las comprobaciones de existencia del vuelo en el controlador de pasajeros.
     *
     * @param flightIdCode clave del HashMap de repositorio de vuelos.
     * @return el vuelo si lo ha encontrado o null si no lo ha encontrado.
     */
    public Flight getFlightIdCode(String flightIdCode) {
        return this.flightService.getFlightIdCode(flightIdCode);
    }

    /**
     * Elimina un pasajero por clave de vuelo y nif del pasajero.
     *
     * @param idCode clave del vuelo  a buscar en el 1 HasMap.
     * @param nif    clave del 2 HashMap
     * @return true si lo ha borrado, false si no lo ha borrado o no existía.
     */
    public boolean deletePassenger(String idCode, String nif) {
        return this.repositoryPassenger.deletePassenger(idCode, nif);
    }

    /**
     * Comprueba que un pasajero exista en un vuelo.
     *
     * @param flightId clave del vuelo.
     * @param nif      clave del pasajero.
     * @return true si existe, false si no existe.
     */
    public boolean existsPassenger(String flightId, String nif) {
        return this.repositoryPassenger.existPassenger(flightId, nif);
    }

    /**
     * Modifica un pasajero en un vuelo.
     *
     * @param nif       clave a buscar del pasajero.
     * @param passenger pasajero modificado.
     */
    public void updatePassenger(String nif, Passenger passenger) {
        this.repositoryPassenger.updatePassenger(nif, passenger);
    }

    /**
     * Elimina un vuelo con todos sus pasajeros.
     *
     * @param idCode clave del vuelo a eliminar.
     */
    public void deleteFlight(String idCode) {
        this.repositoryPassenger.deleteFlight(idCode);
    }

    /**
     * Consigue todos los pasajeros de un vuelo concreto.
     *
     * @param flightId clave del vuelo a buscar en el HashMap.
     * @return la lista de pasajeros, con datos o vacía.
     */
    public List<Passenger> getPassengers(String flightId) {
        return this.repositoryPassenger.listPassengers(flightId);
    }

}
