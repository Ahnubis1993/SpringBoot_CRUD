package org.example.api;

import org.example.core.Client;
import org.example.exceptions.flightException.FlightExists;
import org.example.exceptions.flightException.FlightNotFound;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConnectionFlight {

    /**
     * Construye una petición GET para conseguir un vuelo.
     *
     * @param url EndPoint a buscar en el servidor.
     * @return El vuelo o vuelos en el cuerpo Json y httpStatus=200.
     * @throws Exception Sí falla la petición al servidor.
     *                   FlightNotFound si el vuelo no existe y httpStatus=404
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doGet(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", Client.headerAuth)
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
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
     * Construye petición POST para insertar un vuelo.
     *
     * @param body vueloDTO en json.
     * @param url  EndPoint a buscar en el servidor.
     * @return Mensaje si se crea correctamente y httpStatus=201.
     * @throws Exception Sí falla la petición al servidor.
     *                   BadRequest sí falta algún atributo del pasajero y httpStatus=400, devuelve cúales datos están mal formados.
     *                   FlightExists sí el vuelo existe ya, para no pisarlo un httpStatus=409 conflict.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doPost(String body, String url) throws Exception {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Define el tipo de contenido del cuerpo de la solicitud
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", Client.headerAuth)
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return response.body();
            } else if (response.statusCode() == 400) {
                throw new BadRequest(response.body());
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 409) {
                throw new FlightExists();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }
        }
    }

    /**
     * Construye petición DELETE para eliminar un vuelo.
     *
     * @param url EndPoint a buscar en el servidor.
     * @return Mensaje si borra el vuelo y httpStatus=200.
     * @throws Exception Si falla la petición.
     *                   FlightNotFound si no existe el vuelo y httpStatus=404.
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
     * Construye petición PUT mediante PathVariable idCode y envía en el cuerpo el vuelo
     * para modificarlo.
     *
     * @param body vuelo en Json.
     * @param url  endpoint a buscar en vuelos.
     * @return mensaje del servidor si OK.
     * @throws Exception Sí falla la petición al servidor.
     *                   BadRequest sí falta algún atributo del pasajero y httpStatus=400, devuelve cúales datos están mal formados.
     *                   FlightNotFound sí el vuelo no existe, httpStatus=404 conflict.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     */
    public String doPut(String body, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Define el tipo de contenido del cuerpo de la solicitud
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", Client.headerAuth)
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 400) {
                throw new BadRequest(response.body());
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 404) {
                throw new FlightNotFound();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else {
                throw new Exception();
            }
        }
    }
}