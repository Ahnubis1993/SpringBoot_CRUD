package org.educa.airline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Clase que representa los vuelos recibidos por el cliente.
 * Se verifican que los campos no estén vacíos. La fecha se controla en el cliente.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    @NotBlank(message = "El idCode del vuelo no puede estar vacío")
    private String idCode;
    @NotBlank(message = "El id del vuelo no puede estar vacío")
    private String id;
    @NotBlank(message = "El origen del vuelo no puede estar vacío")
    private String origin;
    @NotBlank(message = "El destino del vuelo no puede estar vacío")
    private String destination;
    @NotNull(message = "La fecha del vuelo no puede ser null")
    private Date date;
}
