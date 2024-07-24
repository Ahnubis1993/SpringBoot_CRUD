package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa el dato pasajero a enviar en las peticiónes HTTP del cliente con el servidor.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private String nif;
    private String flightIdCode;
    private String name;
    private String surname;
    private String email;
    private int seatNumber;
}