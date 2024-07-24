package org.example.exceptions.generalException;

/**
 * Excepción para recoger códigos 400 en vuelos y pasajeros.
 */
public class BadRequest extends RuntimeException {
    private final String message;

    public BadRequest(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
