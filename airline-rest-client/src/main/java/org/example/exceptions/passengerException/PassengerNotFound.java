package org.example.exceptions.passengerException;

/**
 * Excepción para recoger códigos 409 en pasajeros menos en el doPost que se encarga PassengerExists.
 * El pasajero no existe, se utiliza para GET, PUT y DELETE
 */
public class PassengerNotFound extends Exception {
}
