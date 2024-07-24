package org.educa.airline.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase PassengerEntity que representan los datos del repositorio de pasajeros.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @NotBlank
    private String nif;
    @NotBlank
    private String flightIdCode;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String email;
    @NotNull
    private int seatNumber;
}
