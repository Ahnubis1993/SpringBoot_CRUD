package org.example.api;

import org.example.core.Client;
import org.example.exceptions.flightException.FlightNotFound;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;
import org.example.exceptions.passengerException.PassengerExists;
import org.example.exceptions.passengerException.PassengerNotFound;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConnectionPassenger {

    /**
     * Construye petición GET para conseguir uno o varios pasajeros de un vuelo.
     *
     * @param url EndPoint a buscar en el servidor.
     * @return Pasajero o pasajeros del vuelo en el cuerpo json y httpStatus=200.
     * @throws Exception Sí falla la petición al servidor.
     *                   PassengerNotFound sí no hay pasajeros o pasajeros en el vuelo y httpStatus=409.
     *                   FlightNotFound sí el vuelo no existe y httpStatus=404.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doGet(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Authorization", Client.headerAuth)
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 409) {
                throw new PassengerNotFound();
            } else if (response.statusCode() == 404) {
                throw new FlightNotFound();
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }

        }
    }

    /**
     * Construye petición POST para insertar un pasajero en un vuelo concreto.
     *
     * @param body VueloDTO en json.
     * @param url  EndPoint a buscar en el servidor.
     * @return Mensaje si se crea el pasajero y httpStatus=201.
     * @throws Exception Sí falla la petición al servidor.
     *                   BadRequest sí algún atributo del pasajero está mal formado y httpStatus=400.
     *                   FlightNotFound si el vuelo no existe y httpStatus=404.
     *                   PassengerExists si el pasajero ya existe en el vuelo y httpStatus=409.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doPost(String body, String url) throws Exception {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Authorization", Client.headerAuth)
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return response.body();
            } else if (response.statusCode() == 400) {
                if (response.body().startsWith("Mensaje")) {
                    throw new BadRequest(response.body());
                } else {
                    throw new BadRequest(" Petición mal construida. Debes introducir el id del vuelo.");
                }
            } else if (response.statusCode() == 404) {
                throw new FlightNotFound();
            } else if (response.statusCode() == 409) {
                throw new PassengerExists();
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }

        }
    }

    /**
     * Construye petición DELETE para borrar un pasajero de un vuelo.
     *
     * @param url EndPoint a buscar en el servidor.
     * @return Mensaje si se elimina el pasajero del vuelo y httpStatus=200.
     * @throws Exception Sí falla la petición.
     *                   PassengerNotFound si el pasajero a borrar no existe y httpStatus=409.
     *                   FlightNotFound si el vuelo no existe y httpStatus=404.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doDelete(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Authorization", Client.headerAuth)
                .build();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 409) {
                throw new PassengerNotFound();
            } else if (response.statusCode() == 404) {
                throw new FlightNotFound();
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }
        }
    }

    /**
     * Construye una petición PUT para modificar un pasajero.
     *
     * @param body Pasajero en el cuerpo del json.
     * @param url  EndPoint a buscar en el servidor.
     * @return Mensaje si se ha modificado correctamente y httpStatus=200.
     * @throws Exception Sí falla la petición al servidor.
     *                   BadRequest si algún campo está mal introducido y httpStatus=400.
     *                   FlightNotFound si el vuelo no existe y httpStatus=404.
     *                   PassengerNotFound si el pasajero no existe en ese vuelo y httpStatus=409.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doPut(String body, String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Authorization", Client.headerAuth)
                .build();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 400) {
                if (response.body().startsWith("Mensaje")) {
                    throw new BadRequest(response.body());
                } else {
                    throw new BadRequest(" Petición mal construida. Debes introducir el id del vuelo.");
                }
            } else if (response.statusCode() == 404) {
                throw new FlightNotFound();
            } else if (response.statusCode() == 409) {
                throw new PassengerNotFound();
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }
        }
    }

}
