package org.educa.airline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase RoleDTO que representan los roles que el usuario envía con el usuario.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String rol;
    private String name;
    private String description;

}