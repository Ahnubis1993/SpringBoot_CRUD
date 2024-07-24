package org.educa.airline.mappers;

import org.educa.airline.dto.PassengerDTO;
import org.educa.airline.entity.Passenger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de transformar los Objetos Passenger a DTO y viceversa.
 */
@Component
public class PassengerMapper {

    /**
     * Convierte un pasajeroDTO que envía el cliente a una entidad.
     *
     * @param passengerDTO pasajero que envía el cliente.
     * @return pasajeroEntity.
     */
    public Passenger passengerToEntity(PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        passenger.setNif(passengerDTO.getNif());
        passenger.setFlightIdCode(passengerDTO.getFlightIdCode());
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setSeatNumber(passengerDTO.getSeatNumber());
        return passenger;
    }

    /**
     * Convierte un pasajeroEntity (datos del HashMap del repositorio pasajeros) a DTO.
     *
     * @param passenger pasajeroEntity.
     * @return pasajeroDTO.
     */
    public PassengerDTO passengerToDTO(Passenger passenger) {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setNif(passenger.getNif());
        passengerDTO.setFlightIdCode(passenger.getFlightIdCode());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setSurname(passenger.getSurname());
        passengerDTO.setEmail(passenger.getEmail());
        passengerDTO.setSeatNumber(passenger.getSeatNumber());
        return passengerDTO;
    }

    /**
     * Convierte una lista de entidades (datos del HashMap del repositorio de pasajeros).
     *
     * @param passengers pasajerosEntities.
     * @return lista pasajerosDTO.
     */
    public List<PassengerDTO> passengersToDTOs(List<Passenger> passengers) {
        List<PassengerDTO> passengersDTO = new ArrayList<>();
        for (Passenger passenger : passengers) {
            PassengerDTO passengerDTO = passengerToDTO(passenger);
            passengersDTO.add(passengerDTO);
        }
        return passengersDTO;
    }

}
