package org.example.api;

import com.google.gson.Gson;
import org.example.dto.PassengerDTO;

import java.util.Arrays;
import java.util.List;

public class ApiPassengerService extends ApiService {

    /**
     * Url (proporcionada por ApiService) + /flights/{idCode}/passengers para acceder al controlador de pasajeros.
     */
    private final String URL = super.URL + "/flights/{idCode}/passengers";

    /**
     * Añade un pasajero a un vuelo en el servidor.
     *
     * @param passengerDTO Pasajero en el cuerpo de la petición.
     * @param idCode       IdCode del vuelo a introducir el pasajero.
     * @return Respuesta del servidor o error a capturar.
     * @throws Exception Lanza una excepción si ocurre un error en la petición.
     */
    public String addPassenger(PassengerDTO passengerDTO, String idCode) throws Exception {
        String URLAdd = URL.replace("{idCode}", idCode);
        Gson gson = new Gson();
        String jsonString = gson.toJson(passengerDTO);
        return connectionPassenger.doPost(jsonString, URLAdd);
    }

    /**
     * Consigue un pasajero por idCode de vuelo y nif del pasajero.
     *
     * @param idCode IdCode del vuelo a buscar.
     * @param nif    Nif del pasajero a buscar en el vuelo.
     * @return El pasajero o error a capturar.
     * @throws Exception Lanza una excepción si ocurre algún error en el servidor.
     */
    public PassengerDTO getPassenger(String idCode, String nif) throws Exception {
        String URLGet = URL.replace("{idCode}", idCode);
        String body = connectionPassenger.doGet(URLGet + "/" + nif);
        Gson gson = new Gson();
        return gson.fromJson(body, PassengerDTO.class);
    }

    /**
     * Elimina un pasajero de un vuelo.
     *
     * @param idCode IdCode del vuelo a buscar.
     * @param nif    Nif del pasajero a buscar.
     * @throws Exception Lanza una excepción si ocurre algún error en la petición al servidor.
     */
    public String deletePassenger(String idCode, String nif) throws Exception {
        String URLDelete = URL.replace("{idCode}", idCode);
        return connectionPassenger.doDelete(URLDelete + "/" + nif);
    }

    /**
     * Modifica un pasajero en un vuelo.
     *
     * @param passengerDTO Pasajero a modificar en el vuelo.
     * @param nif          Nif del pasajero a buscar en el vuelo.
     * @return Respuesta del servidor o error a capturar.
     * @throws Exception Lanza una excepción si ocurre algún error en el servidor.
     */
    public String updatePassenger(PassengerDTO passengerDTO, String nif) throws Exception {
        String URLDelete = URL.replace("{idCode}", passengerDTO.getFlightIdCode());
        Gson gson = new Gson();
        String jsonString = gson.toJson(passengerDTO);
        return connectionPassenger.doPut(jsonString, URLDelete + "/" + nif);
    }

    /**
     * Consigue todos los pasajeros de un vuelo en concreto.
     *
     * @param idCode IdCode el vuelo a buscar.
     * @return lista de pasajeros o pasajero del vuelo o error a capturar.
     * @throws Exception Lanza una excepción si ocurre algún error en el servidor.
     */
    public List<PassengerDTO> getPassengers(String idCode) throws Exception {
        String URLGet = URL.replace("{idCode}", idCode);
        String body = connectionPassenger.doGet(URLGet);
        Gson gson = new Gson();
        PassengerDTO[] passengersArray = gson.fromJson(body, PassengerDTO[].class);
        return Arrays.asList(passengersArray);
    }
}
