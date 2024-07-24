package org.educa.airline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Clase Role que representan los roles del User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private String rol;
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return rol;
    }
}
