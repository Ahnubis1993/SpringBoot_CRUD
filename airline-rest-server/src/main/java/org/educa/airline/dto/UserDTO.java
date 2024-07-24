package org.educa.airline.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.educa.airline.entity.Role;

import java.util.List;

/**
 * Clase UserDTO que representan los datos que el usuario envía.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;
    @NotBlank(message = "La contraseña del usuario no puede estar vacío")
    private String password;
    @NotBlank(message = "El NIF del usuario no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String nif;
    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    private String name;
    @NotBlank(message = "El apellido del usuario no puede estar vacío")
    private String surname;
    @Email(message = "El email del usuario debe tener el formato correcto (nombre@dominio.(com/net))")
    private String email;
    @NotNull(message = "El rol del usuario no puede estar vacío")
    private List<Role> roles;
}
