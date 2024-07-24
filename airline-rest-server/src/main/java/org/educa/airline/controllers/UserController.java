package org.educa.airline.controllers;

import jakarta.validation.Valid;
import org.educa.airline.dto.UserDTO;
import org.educa.airline.mappers.UserMapper;
import org.educa.airline.services.inservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Clases a inyectar en el constructor.
     */
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Se inyectan ambas clases para hacer uso de las mismas en esta capa.
     *
     * @param userService uso de la capa del servicio para acceder al repositorio,
     * @param userMapper  uso de mapper para convertir los usuarios de DTO a Entity y viceversa.
     */
    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Crea un usuario desde el cliente, se permite la creación de usuarios sin ningún tipo
     * de autenticación o permiso.
     *
     * @param userDTO       usuario a crear.
     * @param bindingResult comprobar que el cuerpo del usuario está bien formado.
     * @return httpStatus 201 (creado).
     * httpStatus 400 (BadRequest).
     * httpStatus 409 (Conflict).
     */
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (this.userService.existsUser(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            this.userService.createUser(this.userMapper.userDTOtoEntity(userDTO));
            return ResponseEntity.status(201).body("Usuario:" + userDTO.getName() + " creado correctamente");
        }
    }

    /**
     * Elimina un usuario mediante autenticación requerida. Además, se debe ser el mismo usuario
     * o tener permisos de admin para poder eliminar el usuario desde otro.
     *
     * @param surname nombreUsuario a buscar para borrar.
     * @return httpStatus 401 (no autenticado).
     * httpStatus 403 (no tiene los permisos o no es el mismo usuario).
     * httpStatus 404 (NotFound).
     * httpStatus 200 (Ok) y el pasajero.
     */
    @DeleteMapping("/{surname}")
    public ResponseEntity<String> getUsers(@PathVariable("surname") String surname) {

        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Obtener detalles del usuario autenticado y su rol
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));

        // Si no es el propio usuario y si es otro usuario que no tenga el rol admin
        // sale, si es el propio usuario o tiene rol admin (pudiendo ser otro usuario)
        if (!userDetails.getUsername().equals(surname) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verificamos que existe o no
        if (this.userService.existsUser(surname)) {
            this.userService.deleteUser(surname);
            return ResponseEntity.ok("Usuario:" + surname + " borrado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca un usuario por nombreUsuario, para ello el usuario se debe haber autenticado primero,
     * ser el propio usuario o tener los permisos admin o personal desde otro.
     *
     * @param surname nombreUsuario s buscar.
     * @return httpStatus 401 (no autenticado).
     * httpStatus 403 (no tiene los permisos o no es el mismo usuario).
     * httpStatus 404 (NotFound).
     * httpStatus 200 (Ok) y el pasajero.
     */
    @GetMapping("/{surname}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("surname") String surname) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Obtener detalles del usuario autenticado y su rol
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));
        boolean isPersonal = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_personal"));

        if (!userDetails.getUsername().equals(surname) && !isAdmin && !isPersonal) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (this.userService.existsUser(surname)) {
            return ResponseEntity.ok(this.userMapper.UserEntitytoDTO(this.userService.getUser(surname)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Actualiza un usuario mediante su nombreUsuario. Para ello en el cuerpo se trae un nuevo usuario
     * con el mismo nombreUsuario, ya que es la clave del hashmap.
     * Para poder actualizar al usuario debe ser él mismo o tener permiso de admin si fuera otro.
     *
     * @param surname       nombreUsuario a buscar.
     * @param userDTO       usuario a modificar.
     * @param bindingResult corrobora que todos los campos del usuario estén bien formados.
     * @return httpStatus 401 (no autenticado).
     * httpStatus 403 (no tiene los permisos o no es el mismo usuario).
     * httpStatus 400 (BadRequest, usuario mal formado).
     * httpStatus 404 (NotFound).
     * httpStatus 200 (Ok) y el pasajero.
     */
    @PutMapping("/{surname}")
    public ResponseEntity<String> updateUser(@PathVariable("surname") String surname,
                                             @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Obtener detalles del usuario autenticado y su rol
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));

        if (!userDetails.getUsername().equals(surname) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (this.userService.existsUser(surname)) {
            // Una vez se comprueba que el usuario puede acceder, si el cuerpo está mal formada,
            // se indica aquí.
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
                return ResponseEntity.badRequest().body(errorMessage);
            }
            this.userService.updateUser(this.userMapper.userDTOtoEntity(userDTO));
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
