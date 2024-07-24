package org.educa.airline.repository.inmemory;

import org.educa.airline.entity.Passenger;
import org.educa.airline.repository.PassengerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryPassengerRepository implements PassengerRepository {
    // Por cada vuelo, habrá un mapa de pasajeros
    private final Map<String, Map<String, Passenger>> passengers = new HashMap<>();

    @Override
    public List<Passenger> listPassengers() {
        return passengers
                .values()
                .stream()
                .flatMap(m -> m.values().stream())
                .collect(Collectors.toList());
    }

    /**
     * Dado un número de vuelo, devuelve sus pasajeros
     *
     * @param flightId Id flight
     * @return La {@link List} de {@link Passenger}
     */
    @Override
    public List<Passenger> listPassengers(String flightId) {
        return new ArrayList<>(getFlightPassengers(flightId).values());
    }

    @Override
    public Passenger getPassenger(String flightId, String nif) {
        return getFlightPassengers(flightId).get(nif);
    }

    /**
     * Dado un número de vuelo y un nif, devuelve si el pasajero está en el vuelo
     * Este método es importante, pues verifica que el pasajero exista en un vuelo
     * que previamente también ha sido comprobado. Se utiliza antes de update por ejemplo,
     * para verificar que el vuelo existe, pero el pasajero no.
     *
     * @param flightId id del vuelo
     * @param nif      el nif del pasajero
     * @return True si existe el pasajero falso en otro caso
     */
    @Override
    public boolean existPassenger(String flightId, String nif) {
        return getFlightPassengers(flightId).containsKey(nif);
    }

    /**
     * Elimina un pasajero de un vuelo
     *
     * @param flightId el identificador del vuelo
     * @param nif      el nif del pasajero
     * @return true si se ha eliminado y
     */
    @Override
    public synchronized boolean deletePassenger(String flightId, String nif) {
        return getFlightPassengers(flightId).remove(nif) != null;
    }

    /**
     * Añada un pasajero al vuelo. Devuelve False si el pasajero ya existe en el vuelo.
     * Invoca al método existsPassenger para verificar que no existe, importante.
     *
     * @param passenger pasajero ma insertar.
     * @return true si se ha insertado, false si no se ha insertado.
     */
    @Override
    public synchronized boolean addPassenger(Passenger passenger) {
        if (existPassenger(passenger.getFlightIdCode(), passenger.getNif())) {
            return false;
        } else {
            getFlightPassengers(passenger.getFlightIdCode()).put(passenger.getNif(), passenger);
            return true;
        }
    }

    /**
     * Actualiza los datos de un pasajero en un vuelo. Devuelve False si el
     * pasajero no existe en el vuelo
     *
     * @param passenger pasajero a modificar.
     */
    @Override
    public synchronized void updatePassenger(String nif, Passenger passenger) {
        getFlightPassengers(passenger.getFlightIdCode()).remove(nif);
        getFlightPassengers(passenger.getFlightIdCode()).put(passenger.getNif(), passenger);
    }

    /**
     * Se utiliza para obtener los pasajeros de un vuelo específico.
     *
     * @param flightId clave del vuelo a buscar en el HashMap.
     * @return un mapa de pasajeros encontrados.
     */

    private Map<String, Passenger> getFlightPassengers(String flightId) {
        passengers.putIfAbsent(flightId, new HashMap<>());
        return passengers.get(flightId);
    }

    /**
     * Elimina un vuelo del HashMap mediante el idCode (clave).
     *
     * @param flightId clave a buscar en el HashMap.
     */
    public synchronized void deleteFlight(String flightId) {
        // Elimina el vuelo del mapa de pasajeros
        passengers.remove(flightId);
        // Devuelve true si el vuelo existía y fue eliminado correctamente
    }
}
