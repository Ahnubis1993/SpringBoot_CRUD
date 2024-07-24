package org.educa.airline.services.inservice;

import org.educa.airline.entity.User;
import org.educa.airline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    /**
     * Clases a inyectar en el constructor.
     */
    private final UserRepository userRepository;
    private final SecurityService securityService;

    /**
     * Se inyectan ambas clases para hacer uso de las mismas en esta capa.
     *
     * @param userRepository  Repositorio de usuarios.
     * @param securityService servicio de seguridad.
     */
    @Autowired
    public UserService(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    /**
     * Cuando el usuario se autentica con este método es invocado para cargar el usuario de la base de datos.
     *
     * @param username nombreUsuario a buscar.
     * @return usuario encontrado.
     * @throws UsernameNotFoundException si no encuentra al usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getUser(username);
        System.out.println("Usuario encontrado:" + user.getPassword());
        return user;
    }

    /**
     * Crea un usuario.
     *
     * @param user usuario a insertar.
     */
    public void createUser(User user) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Codificamos el email y NIF y hacemos un hash de la contraseña
        user.setEmail(this.securityService.crypt(user.getEmail()));
        user.setNif(this.securityService.crypt(user.getNif()));
        user.setPassword(this.securityService.encode(user.getPassword()));
        this.userRepository.createUser(user);
    }

    /**
     * Comprueba que un usuario existe en la base de datos.
     *
     * @param userName nombreUsuario a buscar.
     * @return true si es encontrado y false si no existe.
     */
    public boolean existsUser(String userName) {
        return this.userRepository.existUser(userName);
    }

    // Método a borrar, no se tiene encuentra, solo para comprobar.
    public List<User> getUsers() {
        return this.userRepository.getUsers();
    }

    /**
     * Borra un usuario de la base de datos mediante el nombreUsuario.
     *
     * @param surname nombreUsuario a buscar.
     */
    public void deleteUser(String surname) {
        this.userRepository.deleteUser(surname);
    }

    /**
     * Obtiene un usuario de la base de datos mediante el nombreUsuario.
     *
     * @param surname nombreUsuario a buscar.
     * @return usuario encontrado.
     */
    public User getUser(String surname) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        User user = this.userRepository.getUser(surname);
        if (user != null) {
            // Crear un nuevo objeto User con la información desencriptada
            // Es obligatorio, el usuario recuperado no puede ser modificado nunca.
            User decryptedUser = new User();
            decryptedUser.setName(user.getName());
            decryptedUser.setUsername(user.getUsername());
            decryptedUser.setSurname(user.getSurname());
            decryptedUser.setPassword(null);
            decryptedUser.setNif(this.securityService.decrypt(user.getNif()));
            decryptedUser.setEmail(this.securityService.decrypt(user.getEmail()));
            decryptedUser.setRoles(user.getRoles());
            // No establecer la contraseña para que no se devuelva en la respuesta

            return decryptedUser;
        } else {
            return null;
        }
    }

    /**
     * Modificar un usuario de la base de datos.
     *
     * @param user usuario a modificar.
     */
    public void updateUser(User user) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Codificamos el email y NIF y hacemos un hash de la contraseña
        user.setEmail(this.securityService.crypt(user.getEmail()));
        user.setNif(this.securityService.crypt(user.getNif()));
        user.setPassword(this.securityService.encode(user.getPassword()));
        this.userRepository.updateUser(user);
    }
}
