package org.example.core;

import org.example.service.FlightService;
import org.example.service.PassengerService;
import org.example.service.UserService;

import java.util.Base64;
import java.util.Scanner;

public class Client {

    /**
     * Clases de servicio de vuelos, pasajeros y usuarios para el requerimiento de datos
     * antes de realizar la consulta.
     */
    private final FlightService flightService = new FlightService();
    private final PassengerService passengerService = new PassengerService();
    private final UserService userService = new UserService();
    public static String headerAuth;

    public void run() {
        String option = "";
        try (Scanner sc = new Scanner(System.in)) {
            // Antes del bucle, para que solo pida una vez el login de inicio
            headerAuth = getBasicAuthenticationHeader(sc);
            while (!option.equalsIgnoreCase("0")) {
                option = clientMenu(sc);
                switch (option) {
                    case "0":
                        System.out.println("Fin del programa");
                        break;
                    case "1":
                        chooseOptionFlight(sc);
                        break;
                    case "2":
                        chooseOptionPassenger(sc);
                        break;
                    case "3":
                        chooseOptionUser(sc);
                        break;
                    case "4":
                        headerAuth = getBasicAuthenticationHeader(sc);
                        break;
                    default:
                        System.out.println("Opción incorrecta. Por favor, elija una opción válida.\n");
                }
            }
        }
    }

    private String clientMenu(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Bienvenido a Vuelos Iberia");
        System.out.println("------------------------------");
        System.out.println("0 - Salir");
        System.out.println("1 - Gestión de Vuelos");
        System.out.println("2 - Gestión de Pasajeros");
        System.out.println("3 - Gestión de Usuarios");
        System.out.println("4 - Login Nuevo Usuario");
        System.out.println("Introduce la operación que deseas realizar: ");
        return sc.nextLine();
    }

    private void chooseOptionFlight(Scanner sc) {
        String optionMenuFlight = "";
        while (!optionMenuFlight.equalsIgnoreCase("0")) {
            optionMenuFlight = flightMenu(sc);
            switch (optionMenuFlight) {
                case "0":
                    System.out.println("Volviendo al menú principal.");
                    break;
                case "1":
                    this.flightService.createFight(sc);
                    break;
                case "2":
                    this.flightService.deleteFlight(sc);
                    break;
                case "3":
                    this.flightService.getFlights(sc);
                    break;
                case "4":
                    this.flightService.getFlight(sc);
                    break;
                case "5":
                    this.flightService.modifyFlight(sc);
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor, elija una opción válida.");
            }
        }
    }

    private String flightMenu(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Menú de Gestión de Vuelos");
        System.out.println("------------------------------");
        System.out.println("0 - Volver al menú principal");
        System.out.println("1 - Crear un vuelo");
        System.out.println("2 - Borrar un vuelo");
        System.out.println("3 - Consultar vuelos por origen y destino");
        System.out.println("4 - Consultar vuelo por ID y fecha");
        System.out.println("5 - Modificación de vuelo");
        System.out.println("Introduce la operación que deseas realizar: ");
        return sc.nextLine();
    }

    private void chooseOptionPassenger(Scanner sc) {
        String optionMenuPassenger = "";
        while (!optionMenuPassenger.equalsIgnoreCase("0")) {
            optionMenuPassenger = passengerMenu(sc);
            switch (optionMenuPassenger) {
                case "0":
                    System.out.println("Volviendo al menú principal.");
                    break;
                case "1":
                    this.passengerService.addPassenger(sc);
                    break;
                case "2":
                    this.passengerService.getPassenger(sc);
                    break;
                case "3":
                    this.passengerService.updatePassenger(sc);
                    break;
                case "4":
                    this.passengerService.deletePassenger(sc);
                    break;
                case "5":
                    this.passengerService.getPassengers(sc);
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor, elija una opción válida.");
            }
        }
    }

    private String passengerMenu(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Menú de Gestión de Pasajeros");
        System.out.println("------------------------------");
        System.out.println("0 - Volver al menú principal");
        System.out.println("1 - Asociar pasajero a un vuelo");
        System.out.println("2 - Búsqueda de un pasajero");
        System.out.println("3 - Modificar un pasajero");
        System.out.println("4 - Eliminar un pasajero");
        System.out.println("5 - Buscar todos los pasajeros de un vuelo");
        System.out.println("Introduce la operación que deseas realizar: ");
        return sc.nextLine();
    }

    private void chooseOptionUser(Scanner sc) {
        String optionMenuUser = "";
        while (!optionMenuUser.equalsIgnoreCase("0")) {
            optionMenuUser = userMenu(sc);
            switch (optionMenuUser) {
                case "0":
                    System.out.println("Volviendo al menú principal.");
                    break;
                case "1":
                    this.userService.createUser(sc);
                    break;
                case "2":
                    this.userService.modifyUser(sc);
                    break;
                case "3":
                    this.userService.deleteUser(sc);
                    break;
                case "4":
                    this.userService.getUser(sc);
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor, elija una opción válida.");
            }
        }
    }

    private String userMenu(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("Menú de Gestión de Usuarios");
        System.out.println("------------------------------");
        System.out.println("0 - Volver al menú principal");
        System.out.println("1 - Crear usuario");
        System.out.println("2 - Modificar usuario");
        System.out.println("3 - Eliminar usuario");
        System.out.println("4 - Consultar usuario");
        System.out.println("Introduce la operación que deseas realizar: ");
        return sc.nextLine();
    }

    private static String getBasicAuthenticationHeader(Scanner sc) {
        System.out.println("------------------------------");
        System.out.println("            Login");
        System.out.println("------------------------------");
        System.out.println("Introduce el nombre de usuario a autenticar: ");
        String username = sc.nextLine();
        System.out.println("Introduce la contraseña para este usuario: ");
        String password = sc.nextLine();
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }


}