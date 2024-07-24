package org.educa.airline.mappers;

import org.educa.airline.dto.FlightDTO;
import org.educa.airline.entity.Flight;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de transformar los Objetos Flight a DTO y viceversa.
 */

@Component
public class FlightMapper {

    /**
     * Convierte un objeto Flight a DTO.
     *
     * @param flight vuelo extraído del HashMap (Repositorio de vuelos).
     * @return el vuelo en DTO.
     */
    public FlightDTO flightToDTO(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setIdCode(flight.getIdCode());
        flightDTO.setId(flight.getId());
        flightDTO.setDate(flight.getDate());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        return flightDTO;
    }

    /**
     * Convierte una Lista de FlightEntities a DTO.
     *
     * @param flights vuelos extraídos del HashMap (Repositorio de vuelos).
     * @return lista de vuelos en DTO.
     */
    public List<FlightDTO> flightsToTDO(List<Flight> flights) {
        List<FlightDTO> flightsDTOs = new ArrayList<>();
        for (Flight flight : flights) {
            FlightDTO flightDTO = flightToDTO(flight);
            flightsDTOs.add(flightDTO);
        }
        return flightsDTOs;
    }

    /**
     * Convierte un objeto FlightDTO a entidad Flight.
     *
     * @param flightDTO vueloDTO.
     * @return la entidad de vuelo.
     */
    public Flight flightDTOtoEntity(FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setIdCode(flightDTO.getIdCode());
        flight.setId(flightDTO.getId());
        flight.setDate(flightDTO.getDate());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        return flight;
    }

}
