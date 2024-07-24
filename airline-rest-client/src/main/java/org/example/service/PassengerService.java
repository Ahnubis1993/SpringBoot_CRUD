package org.example.service;

import org.example.api.ApiPassengerService;
import org.example.dto.PassengerDTO;
import org.example.exceptions.flightException.FlightNotFound;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;
import org.example.exceptions.passengerException.PassengerExists;
import org.example.exceptions.passengerException.PassengerNotFound;

import java.util.List;
import java.util.Scanner;

public class PassengerService {

    /**
     * Clase donde se enviara la información y conectará con el servidor.
     */
    private final ApiPassengerService apiPassengerService = new ApiPassengerService();

    /**
     * Añade un pasajero a un vuelo, para ello se crea con todos sus atributos y se especifíca el vuelo (idCode)
     * donde se quiera insertar. Se recoge la respuesta del servidor, tanto si se ha insertado correctamente,
     * como si se ha producido algún tipo de error.
     *
     * @param sc Para introducir los atributos del pasajero.
     */
    public void addPassenger(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Añadir Pasajero a un Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce idCode único del vuelo: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Introduce nif del pasajero: ");
        String nif = sc.nextLine().trim().toUpperCase();
        System.out.println("Introduce nombre del pasajero: ");
        String name = sc.nextLine().trim();
        System.out.println("Introduce apellidos del pasajero: ");
        String surname = sc.nextLine().trim();
        System.out.println("Introduce email del pasajero: ");
        String email = sc.nextLine().trim();
        System.out.println("Introduce numero de asiento del pasajero: ");
        String seat = sc.nextLine().trim();

        try {
            PassengerDTO passengerDTO = createPassengerDTO(idCode, nif, name, surname, email, seat);
            String request = this.apiPassengerService.addPassenger(passengerDTO, idCode);
            System.out.println("Mensaje del servidor: " + request + "\n");

            // Si has introducido el asiento sin un número
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir el número de asiento a un entero.");
            // Si falta algún campo del pasajero, el servidor te indica cúales.
        } catch (BadRequest b) {
            System.out.println("Mensaje del servidor:" + b.getMessage() + "\n");
            // Si el pasajero existe ya en el vuelo.
        } catch (FlightNotFound f) {
            System.out.println("No existe el vuelo:" + idCode + "\n");
            // Si ocurre algún error en la petición.
        } catch (PassengerExists p) {
            System.out.println("El pasajero:" + nif + " ya existe en el vuelo:" + idCode + "\n");
            // Si el vuelo no existe
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

    }

    /**
     * Consigue un pasajero por idCode (clave de vuelo) y nif del pasajero (identificador único).
     * Si la petición se realiza correctamente, muestra el pasajero. Si no hubiera pasajero o se produjese
     * algún error, se recoge y se muestra el tipo de mensaje.
     *
     * @param sc Introducir los datos a buscar del pasajero.
     */
    public void getPassenger(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Consultar Pasajero en un Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce el idCode del vuelo donde se ubica el pasajero: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Inserta el nif del pasajero a buscar: ");
        String nif = sc.nextLine().trim().toUpperCase();

        try {
            PassengerDTO passengerDTO = this.apiPassengerService.getPassenger(idCode, nif);
            System.out.println("Código de vuelo:" + passengerDTO.getFlightIdCode() + "\nNif:" + passengerDTO.getNif() + "\nNombre:" + passengerDTO.getName()
                    + "\nApellidos:" + passengerDTO.getSurname() + "\nEmail:" + passengerDTO.getEmail() + "\nNªAsiento:" + passengerDTO.getSeatNumber() + "\n");

            // Si no se encuentra el pasajero, no se consigue.
        } catch (FlightNotFound f) {
            System.out.println("No existe el vuelo:" + idCode + "\n");
            // Sí hay algún error en la petición al servidor.
        } catch (PassengerNotFound p) {
            System.out.println("No existe pasajero con nif:" + nif + " en el vuelo:" + idCode + "\n");
            // Si no existe el vuelo.
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    /**
     * Elimina un pasajero mediante el idCode y nif (ambos únicos en sus HashMap del servidor).
     *
     * @param sc Introducir el idCode del vuelo y nif del pasajero a eliminar.
     */
    public void deletePassenger(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Eliminar Pasajero de un Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce el idCode del vuelo donde se está el pasajero: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Inserta el nif del pasajero a buscar: ");
        String nif = sc.nextLine().trim().toUpperCase();

        try {
            String response = this.apiPassengerService.deletePassenger(idCode, nif);
            System.out.println(response);
            // Si no existe el vuelo.
        } catch (FlightNotFound f) {
            System.out.println("No existe el vuelo:" + idCode + "\n");
            // Si el pasajero no existe, no se puede borrar.
        } catch (PassengerNotFound p) {
            System.out.println("No existe el pasajero:" + nif + "\n");
            // Sí se produce algún error en la petición al servidor.
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    /**
     * Actualiza un pasajero, en este caso se debe introducir el idCode del vuelo en caso de que se
     * desee cambiar al pasajero de vuelo, por error del cliente por ejemplo, se debe dejar cambiar a un pasajero
     * de vuelo. Además, se insertan el resto de campos. El nif es el identificador del pasajero en el vuelo,
     * no debería ser modificado, si no, no localizará al pasajero.
     * Se recoge la respuesta del servidor y se muestra si se ha actualizado correctamente o recoge el error y
     * muestra un mensaje.
     *
     * @param sc Introducir los nuevos campos del pasajero.
     */
    public void updatePassenger(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Actualizar Información de Pasajero");
        System.out.println("------------------------------");
        System.out.println("Introduce idCode único del vuelo: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Introduce nif del pasajero: ");
        String nif = sc.nextLine().trim().toUpperCase();
        System.out.println("Introduce nombre del pasajero: ");
        String name = sc.nextLine().trim();
        System.out.println("Introduce apellidos del pasajero: ");
        String surname = sc.nextLine().trim();
        System.out.println("Introduce email del pasajero: ");
        String email = sc.nextLine().trim();
        System.out.println("Introduce numero de asiento del pasajero: ");
        String seat = sc.nextLine().trim();

        try {
            PassengerDTO passengerDTO = createPassengerDTO(idCode, nif, name, surname, email, seat);
            String request = this.apiPassengerService.updatePassenger(passengerDTO, nif);
            System.out.println("Mensaje del servidor: " + request + "\n");

            // Si se introduce en el número de asiento un dato que no sea un número.
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir el número de asiento a un entero.");
            // Sí falta algún campo en el cuerpo del pasajero.
        } catch (BadRequest b) {
            System.out.println("Mensaje del servidor:" + b.getMessage() + "\n");
            // Si el pasajero no se encuentra, no se puede modificar.
        } catch (FlightNotFound f) {
            System.out.println("No existe el vuelo:" + idCode + "\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (PassengerNotFound p) {
            System.out.println("El pasajero:" + nif + " no existe en el vuelo:" + idCode + "\n");
            // Si el vuelo no existe.
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    /**
     * Consigue todos los pasajeros de un vuelo concreto. En caso de que hubiera algún error, se captura
     * y se muestra un mensaje.
     *
     * @param sc Introducir el idCode el vuelo.
     */
    public void getPassengers(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Consultar Pasajeros en un Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce idCode único del vuelo: ");
        String idCode = sc.nextLine().trim();

        try {
            List<PassengerDTO> passengersDTO = this.apiPassengerService.getPassengers(idCode);

            for (PassengerDTO passengerDTO : passengersDTO) {
                System.out.println("Código vuelo:" + passengerDTO.getFlightIdCode() + "\nNif:" + passengerDTO.getNif() + "\nNombre:" + passengerDTO.getName()
                        + "\nApellidos:" + passengerDTO.getSurname() + "\nEmail:" + passengerDTO.getEmail() + "\nNªAsiento:" + passengerDTO.getSeatNumber() + "\n");
            }

            // Si el vuelo no existe.
        } catch (FlightNotFound f) {
            System.out.println("No existe el vuelo:" + idCode + "\n");
            // Si se produce un error en la petición al servidor.
        } catch (PassengerNotFound p) {
            System.out.println("No hay pasajeros en el vuelo:" + idCode + "\n");
            // Si el vuelo no existe.
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    /**
     * Crea un pasajeroDTO y lo devuelve. Se usa para crear y actualizar un pasajero (código duplicado).
     *
     * @param idCode  Código único del vuelo.
     * @param nif     DNI del pasajero.
     * @param name    Nombre del pasajero.
     * @param surname Apellidos del pasajero.
     * @param email   Email del pasajero.
     * @param seat    Asiento del pasajero.
     * @return PasajeroDTO creado.
     */
    private PassengerDTO createPassengerDTO(String idCode, String nif, String name, String surname, String email, String seat) {
        int seatNumber = Integer.parseInt(seat);
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFlightIdCode(idCode);
        passengerDTO.setNif(nif);
        passengerDTO.setName(name);
        passengerDTO.setSurname(surname);
        passengerDTO.setEmail(email);
        passengerDTO.setSeatNumber(seatNumber);
        return passengerDTO;
    }

}
