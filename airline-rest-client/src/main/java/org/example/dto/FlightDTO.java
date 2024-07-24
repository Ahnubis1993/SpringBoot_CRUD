package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Clase que representa el dato vuelo a enviar en las peticiónes HTTP del cliente con el servidor.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String idCode;
    private String id;
    private String origin;
    private String destination;
    private Date date;
}