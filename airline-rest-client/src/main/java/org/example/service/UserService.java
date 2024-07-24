package org.example.service;

import org.example.api.ApiUserService;
import org.example.dto.Role;
import org.example.dto.User;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;
import org.example.exceptions.userExceptions.UserExists;
import org.example.exceptions.userExceptions.UserNotFound;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private final ApiUserService apiUserService = new ApiUserService();

    /**
     * Crea un usuario con un rol asignado a elegir y lo envía al servidor para su inserción.
     *
     * @param sc Scanner para introducir los datos del usuario.
     */
    public void createUser(Scanner sc) {
        System.out.println("Ingrese el nombre de usuario:");
        String username = sc.nextLine();
        System.out.println("Ingrese la contraseña:");
        String password = sc.nextLine();
        System.out.println("Ingrese el NIF:");
        String nif = sc.nextLine();
        System.out.println("Ingrese el nombre:");
        String name = sc.nextLine();
        System.out.println("Ingrese el apellido:");
        String surname = sc.nextLine();
        System.out.println("Ingrese el correo electrónico:");
        String email = sc.nextLine();

        User newUser = new User();
        try {
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setNif(nif);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setEmail(email);

            // Roles disponibles
            Role roleAdmin = new Role("ROLE_admin", "admin", "description");
            Role rolePersonal = new Role("ROLE_personal", "personal", "description");
            Role roleUsuario = new Role("ROLE_usuario", "usuario", "description");

            // Mostrar roles disponibles al usuario
            System.out.println("Seleccione un rol:");
            System.out.println("1. Rol Administrador");
            System.out.println("2. Rol Personal");
            System.out.println("3. Rol Usuario");
            System.out.println("Ingrese el número correspondiente al rol:");

            String selectedRole = sc.nextLine();

            // Agregar el rol seleccionado al usuario
            if (selectedRole.equalsIgnoreCase("1")) {
                newUser.setRoles(List.of(roleAdmin));
            } else if (selectedRole.equalsIgnoreCase("2")) {
                newUser.setRoles(List.of(rolePersonal));
            } else if (selectedRole.equalsIgnoreCase("3")) {
                newUser.setRoles(List.of(roleUsuario));
            }

            String response = this.apiUserService.createUserApi(newUser);
            System.out.println(response);

        } catch (UserExists ue) {
            System.out.println("El usuario existe:" + newUser.getUsername() + " ya existe en la base de datos");
        } catch (BadRequest ba) {
            System.out.println("Mensaje del servidor:" + ba.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println("Mensaje: " + e.getMessage());
        }
    }

    /**
     * Modifica un usuario del servidor. Se introduce el nombreUsuario a modificar, ya que si no sería
     * una creación, y después se introducen el resto de datos del usuario con su asignación de rol.
     *
     * @param sc Scanner para introducir los datos a modificar del usuario.
     */
    public void modifyUser(Scanner sc) {
        System.out.println("Primero introduce el username a modificar:");
        String usernameToModify = sc.nextLine();
        System.out.println("Ingrese nueva contraseña:");
        String password = sc.nextLine();
        System.out.println("Ingrese nuevo NIF:");
        String nif = sc.nextLine();
        System.out.println("Ingrese nuevo nombre:");
        String name = sc.nextLine();
        System.out.println("Ingrese nuevo apellido:");
        String surname = sc.nextLine();
        System.out.println("Ingrese nuevo correo electrónico:");
        String email = sc.nextLine();

        User newUser = new User();
        try {
            newUser.setUsername(usernameToModify);
            newUser.setPassword(password);
            newUser.setNif(nif);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setEmail(email);

            // Roles disponibles
            Role roleAdmin = new Role("ROLE_admin", "admin", "description");
            Role rolePersonal = new Role("ROLE_personal", "personal", "description");
            Role roleUsuario = new Role("ROLE_usuario", "usuario", "description");

            // Mostrar roles disponibles al usuario
            System.out.println("Seleccione un rol:");
            System.out.println("1. Rol de Administrador");
            System.out.println("2. Rol Personal");
            System.out.println("3. Rol Usuario");
            System.out.println("Ingrese el número correspondiente al rol:");

            String selectedRole = sc.nextLine();

            // Agregar el rol seleccionado al usuario
            if (selectedRole.equalsIgnoreCase("1")) {
                newUser.setRoles(List.of(roleAdmin));
            } else if (selectedRole.equalsIgnoreCase("2")) {
                newUser.setRoles(List.of(rolePersonal));
            } else if (selectedRole.equalsIgnoreCase("3")) {
                newUser.setRoles(List.of(roleUsuario));
            }

            String response = this.apiUserService.modifyUserApi(newUser);
            System.out.println(response);

        } catch (BadRequest b) {
            System.out.println("Mensaje del servidor:" + b.getLocalizedMessage());
        } catch (UserNotFound un) {
            System.out.println("El usuario:" + usernameToModify + " no existe en la base de datos");
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong r) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina un usuario mediante su nombreUsuario.
     *
     * @param sc Scanner para introducir el nombreUsuario.
     */
    public void deleteUser(Scanner sc) {
        System.out.println("Introduce surname del usuario a eliminar: ");
        String surname = sc.nextLine();
        try {
            String response = this.apiUserService.deleteUserApi(surname);
            System.out.println(response);
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong ro) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (UserNotFound un) {
            System.out.println("El usuario:" + surname + " no existe en la base de datos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Consulta de un usuario para mostrar sus datos.
     *
     * @param sc Scanner para introducir el nombreUsuario a buscar.
     */
    public void getUser(Scanner sc) {
        System.out.println("Introduce surname usuario a buscar: ");
        String surname = sc.nextLine();
        try {
            User user = this.apiUserService.getUser(surname);
            System.out.println("Usuario: " + user.getUsername() + "\n NIF: " + user.getNif() + "\n Nombre: " + user.getName() + "\n Apellido: " + user.getSurname() + "\n Correo electrónico: " + user.getEmail() + "\n Roles: " + user.getRoles());
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong ro) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (UserNotFound un) {
            System.out.println("El usuario:" + surname + " no existe en la base de datos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
