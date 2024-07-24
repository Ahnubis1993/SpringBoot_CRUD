package org.example.api;

import org.example.core.Client;
import org.example.exceptions.generalException.BadRequest;
import org.example.exceptions.generalException.NotAuth;
import org.example.exceptions.generalException.RoleWrong;
import org.example.exceptions.userExceptions.UserExists;
import org.example.exceptions.userExceptions.UserNotFound;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConnectionUser {

    /**
     * Petición POST para crear un usuario. No requiere de autenticación o permisos.
     *
     * @param body usuario a crear.
     * @param url  endpoint a buscar en /user.
     * @return respuesta del servidor en caso Ok.
     * @throws Exception si falla la petición al servidor.
     *                   BadRequest si algún dato del usuario está mal formado.
     *                   UserExists si ya existe.
     */
    public String doPost(String body, String url) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return response.body();
            } else if (response.statusCode() == 400) {
                throw new BadRequest(response.body());
            } else if (response.statusCode() == 409) {
                throw new UserExists();
            } else {
                throw new Exception("Unexpected HTTP status code: " + response.statusCode());
            }
        }
    }

    /**
     * Elimina un usuario del servidor, para ellos se tiene que autenticar (requisito obligatorio).
     *
     * @param url endpoint a buscar en /user.
     * @return respuesta del servidor en caso Ok.
     * @throws Exception sí falla la petición al servidor.
     *                   UserNotFound si el usuario no existe en el servidor.
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
                throw new UserNotFound();
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
     * Obtiene un usuario del servidor. Primero se tiene que autenticar (obligatorio).
     *
     * @param url endpoint a buscar en /user.
     * @return Usuario encontrado.
     * @throws Exception sí falla la petición.
     *                   UserNotFound si el usuario no existe en el servidor.
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
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else if (response.statusCode() == 404) {
                throw new UserNotFound();
            } else {
                throw new Exception();
            }

        }

    }

    /**
     * Modifica un usuario. Se envía en el cuerpo de la petición al usuario con los nuevos datos.
     *
     * @param body Usuario a modificar.
     * @param url  endpoint a buscar en /user.
     * @return respuesta del servidor.
     * @throws Exception sí falla la petición.
     *                   NotAuth si no se ha autenticado.
     *                   RoleWrong si no tiene los permisos de rol.
     *                   BadRequest si algún dato del usuario está mal formado.
     *                   UserNotFound si el usuario no existe en el servidor.
     */
    public String doUpdate(String body, String url) throws Exception {

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
            } else if (response.statusCode() == 401) {
                throw new NotAuth();
            } else if (response.statusCode() == 403) {
                throw new RoleWrong();
            } else if (response.statusCode() == 404) {
                throw new UserNotFound();
            } else if (response.statusCode() == 400) {
                throw new BadRequest(response.body());
            } else {
                throw new Exception();
            }

        }
    }

}
