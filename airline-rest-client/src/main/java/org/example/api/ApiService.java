package org.example.api;

/**
 * Clase para no acoplar la conexión ni la URL.
 */
public abstract class ApiService {
    final String URL = "http://localhost:8080";
    final ConnectionFlight connectionFlight = new ConnectionFlight();
    final ConnectionPassenger connectionPassenger = new ConnectionPassenger();
    final ConnectionUser connectionUser = new ConnectionUser();

}
