package org.educa.airline.mappers;

import org.educa.airline.dto.UserDTO;
import org.educa.airline.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Convierte un usuarioDTO a un usuarioEntity.
     *
     * @param userDTO usuarioDTO.
     * @return usuarioEntity.
     */
    public User userDTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setNif(userDTO.getNif());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRoles());
        return user;
    }

    /**
     * Convierte un usuarioEntity a un usuarioDTO.
     *
     * @param user usuarioEntity.
     * @return usuarioDTO.
     */
    public UserDTO UserEntitytoDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setNif(user.getNif());
        userDTO.setEmail(user.getEmail());

        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

}
