package org.example.service;

import org.example.api.ApiFlightService;
import org.example.dto.FlightDTO;
import org.example.exceptions.flightException.FlightExists;
import org.example.exceptions.flightException.FlightNotFound;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class FlightService {

    /**
     * Clase donde se enviara la información y conectará con el servidor.
     */
    private final ApiFlightService apiFlightService = new ApiFlightService();

    /**
     * El cliente inserta el destino y el origen mediante un scanner,
     * envía estos datos a ApiFlightService para que haga la petición y recoge
     * los datos del server. Recoge el resultado devuelto y si hay vuelos con esos parámetros,
     * se muestran, si no hay vuelos, se captura con FlightNotFound,
     * y si se produce un error en la consulta se captura con Exception.
     *
     * @param sc Inserta los campos que van en la petición.
     */
    public void getFlights(Scanner sc) {
        System.out.println("---------------------------------------");
        System.out.println("Consulta de Vuelos por origen y destino");
        System.out.println("---------------------------------------");
        System.out.println("Introduce el origen del vuelo: ");
        String origin = sc.nextLine().trim();
        System.out.println("Introduce destino del vuelo: ");
        String destination = sc.nextLine().trim();

        try {
            List<FlightDTO> flightsDTO = this.apiFlightService.getFlights(origin, destination);

            for (FlightDTO flight : flightsDTO) {
                System.out.println("Código vuelo:" + flight.getIdCode() + "\nId:" + flight.getId() + "\nFecha:" + flight.getDate()
                        + "\nOrigen:" + flight.getOrigin() + "\nDestino:" + flight.getDestination() + "\n");
            }

            // Si no hay vuelos.
        } catch (FlightNotFound e) {
            System.out.println("No existe ningún vuelo con origen " + origin + ", y destino a " + destination + "\n");
            // Si ocurre algún error en la petición.
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
     * Realiza una petición al servidor por ID y fecha del vuelo.
     * La fecha se introduce mediante el formato dd/MM/yyyy y se envía en el cuerpo
     * de la petición en formato LocalDate, ya que no permite a Date. Captura la excepción si se introduce mal el formato.
     * Se recoge la respuesta del servidor y se imprime el vueloDTO o se captura el error correspondiente.
     * Si ha ocurrido algún error, lo captura y muestra un mensaje dependiendo del tipo error generado.
     *
     * @param sc Inserta id y fecha.
     */
    public void getFlight(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Consulta de Vuelo por ID y Fecha");
        System.out.println("------------------------------");
        System.out.println("Introduce el id del vuelo: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Introduce fecha (dd/MM/yyyy) del vuelo: ");
        String dateString = sc.nextLine().trim();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateString, formatter);
            FlightDTO flightsDTO = this.apiFlightService.getFlight(idCode, date);

            System.out.println("Código vuelo:" + flightsDTO.getIdCode() + "\nId:" + flightsDTO.getId() + "\nFecha:" + flightsDTO.getDate()
                    + "\nOrigen:" + flightsDTO.getOrigin() + "\nDestino:" + flightsDTO.getDestination() + "\n");

            // Si se introduce otro formato de fecha que no es el especificado.
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha incorrecto. Por favor, utiliza el formato dd/MM/yyyy.");
            // Si el vuelo no existe.
        } catch (FlightNotFound e) {
            System.out.println("No existe ningún vuelo con idCode:" + idCode + ", y fecha:" + dateString + "\n");
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
     * Crea un vuelo mediante la introducción de los parámetros que requiere vueloDTO y lo crea.
     * La fecha se formatea para que el usuario introduzca día, mes y año.
     * Una vez se han creado correctamente los campos de vueloDTO, se envía al servidor para su creación.
     * Se recoge la respuesta del servidor. Si ha ocurrido algún error, lo captura y muestra un mensaje
     * dependiendo del tipo error generado.
     *
     * @param sc Para introducir los datos del pasajero.
     */
    public void createFight(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Creación de Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce el código único de vuelo: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Introduce el id e vuelo: ");
        String id = sc.nextLine().trim();
        System.out.println("Introduce origen de vuelo: ");
        String origin = sc.nextLine().trim();
        System.out.println("Introduce destino de vuelo: ");
        String destination = sc.nextLine().trim();
        System.out.println("Introduce fecha de vuelo (dd/MM/yyyy): ");
        String dateString = sc.nextLine().trim();

        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setIdCode(idCode);
            flightDTO.setId(id);
            flightDTO.setOrigin(origin);
            flightDTO.setDestination(destination);
            flightDTO.setDate(simpleFormat.parse(dateString));

            String request = this.apiFlightService.createFlight(flightDTO);
            System.out.println("Mensaje del servidor: " + request + "\n");

            // Si se introduce un formato fecha diferente al especificado.
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto. Por favor, utiliza el formato dd/MM/yyyy.");
        } catch (BadRequest b) {
            // El servidor mostrará los campos que no se han introducido o han fallado.
            System.out.println("Mensaje del servidor: " + b.getLocalizedMessage() + ".\n");
            // Si el vuelo existe ya, no se pisa.
        } catch (FlightExists c) {
            System.out.println("El vuelo:" + idCode + " ya existe. Utiliza un código único de vuelo.\n");
            // Sí ocurre algún error en la petición al servidor.
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
     * Borrar un vuelo por idCode que es único.
     * Se recoge la respuesta del servidor. Si ha ocurrido algún error, lo captura y muestra un mensaje
     * dependiendo del tipo error generado.
     *
     * @param sc Introduce idCode.
     */
    public void deleteFlight(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Borrado de Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce el idCódigo del vuelo a borrar: ");
        String idCode = sc.nextLine().trim();

        try {
            String request = this.apiFlightService.deleteFlight(idCode);
            System.out.println("Respuesta del servidor: " + request);

            // Si vuelo no existe, no se borra.
        } catch (FlightNotFound f) {
            System.out.println("No se encontró ningún vuelo con el idCode:" + idCode + ".\n");
            // Sí ocurre algún error en la petición al servidor.
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
     * Modifica un vuelo por idCode único, para ello se pide el idCode para localizar al vuelo
     * y el resto de campos son modificables.
     *
     * @param sc Scanner para introducir los datos del vuelo.
     */
    public void modifyFlight(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Modificación de Vuelo");
        System.out.println("------------------------------");
        System.out.println("Introduce el código único de vuelo a modificar: ");
        String idCode = sc.nextLine().trim();
        System.out.println("Introduce nuevo id e vuelo: ");
        String id = sc.nextLine().trim();
        System.out.println("Introduce nuevo origen de vuelo: ");
        String origin = sc.nextLine().trim();
        System.out.println("Introduce nuevo destino de vuelo: ");
        String destination = sc.nextLine().trim();
        System.out.println("Introduce nueva fecha de vuelo (dd/MM/yyyy): ");
        String dateString = sc.nextLine().trim();

        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setIdCode(idCode);
            flightDTO.setId(id);
            flightDTO.setOrigin(origin);
            flightDTO.setDestination(destination);
            flightDTO.setDate(simpleFormat.parse(dateString));

            String request = this.apiFlightService.modifyFlight(flightDTO, idCode);
            System.out.println("Mensaje del servidor: " + request + "\n");

            // Si se introduce un formato fecha diferente al especificado.
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto. Por favor, utiliza el formato dd/MM/yyyy.");
        } catch (BadRequest b) {
            // El servidor mostrará los campos que no se han introducido o han fallado.
            System.out.println("Mensaje del servidor: " + b.getLocalizedMessage() + ".\n");
            // Si el vuelo existe ya, no se pisa.
        } catch (FlightNotFound c) {
            System.out.println("El vuelo:" + idCode + " no existe. Utiliza otro código único de vuelo.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (NotAuth na) {
            System.out.println("Acceso no autorizado. Por favor, inicie sesión para acceder a este recurso.\n");
            // Sí ocurre algún error en la petición al servidor.
        } catch (RoleWrong e) {
            System.out.println("Acceso prohibido. Se deniega el acceso a este recurso por motivos de seguridad.");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

    }
}
