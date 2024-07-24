package org.example.api;

import com.google.gson.Gson;
import org.example.dto.User;

public class ApiUserService extends ApiService {
    /**
     * URL hacia el endpoint del servidor para gestionar los usuarios.
     */
    private final String URL = super.URL + "/user";

    /**
     * Crea un usuario mediante la URl especificada y un json con el cuerpo del usuario creado.
     *
     * @param user usuario a inserta en la base de datos.
     * @return respuesta del servidor o error a capturar.
     * @throws Exception en caso de que falle la petición al servidor.
     */
    public String createUserApi(User user) throws Exception {
        Gson gson = new Gson();
        String body = gson.toJson(user);
        return connectionUser.doPost(body, URL);
    }

    /**
     * Modifica un usuario con la URL y su nombreUsuario que será PathVariable de la URL.
     *
     * @param newUser usuario con nuevos datos.
     * @return respuesta del servidor o error a capturar.
     * @throws Exception en caso de que falle la petición.
     */
    public String modifyUserApi(User newUser) throws Exception {
        Gson gson = new Gson();
        String body = gson.toJson(newUser);
        return connectionUser.doUpdate(body, URL + "/" + newUser.getUsername());
    }

    /**
     * Elimina un usuario por nombreUsuario.
     *
     * @param surname nombreUsuario a buscar en el servidor para eliminar.
     * @return respuesta del servidor o error a capturar.
     * @throws Exception en caso de que la petición falle.
     */
    public String deleteUserApi(String surname) throws Exception {
        return connectionUser.doDelete(URL + "/" + surname);
    }

    /**
     * Busca y obtiene un usuario por nombreUsuario.
     *
     * @param surname nombreUsuario.
     * @return el usuario o error a capturar.
     * @throws Exception en caso de que falle la petición al servidor.
     */
    public User getUser(String surname) throws Exception {
        String body = connectionUser.doGet(URL + "/" + surname);
        Gson gson = new Gson();
        return gson.fromJson(body, User.class);
    }
}
