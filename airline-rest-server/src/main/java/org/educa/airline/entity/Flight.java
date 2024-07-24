package org.educa.airline.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Clase FlightEntity que representan los datos del repositorio de vuelos.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @NotBlank
    private String idCode;
    @NotBlank
    private String id;
    @NotBlank
    private String origin;
    @NotBlank
    private String destination;
    @NotNull
    private Date date;
}
