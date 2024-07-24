package org.educa.airline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa los pasajeros recibidos por el cliente.
 * Se verifica que los campos no estén vacíos. El nif tenga 8 dígitos y 1 letra en mayúsculas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    @NotBlank(message = "El nif del pasajero no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String nif;
    @NotBlank(message = "El idCode del vuelo no puede estar vacío")
    private String flightIdCode;
    @NotBlank(message = "El nombre del pasajero no puede estar vacío")
    private String name;
    @NotBlank(message = "El apellidos del pasajero no puede estar vacío")
    private String surname;
    @NotBlank(message = "El email del pasajero no puede estar vacío")
    // 1 Parte del email puede contener los siguientes carácteres: a-z, A-Z, números del 0 al 9 y carácteres especiales.
    // Luego se concatena % (caracteres anteriores al símbolo arroba) + @ + 2 parte = mismo formato que la primera parte.
    // Después de la segunda parte se suma el dominio ("\\.") + (caracteres: com/net). La cadena termina en $.
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El correo electrónico debe tener un formato válido como usuario@dominio.(net/com..))")
    private String email;
    @NotNull(message = "El numero de asiento no puede estar vacío")
    private int seatNumber;
}
