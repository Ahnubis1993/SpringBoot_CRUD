package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dto.FlightDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ApiFlightService extends ApiService {
    /**
     * Url (proporcionada por ApiService) + /flights para acceder al controlador de vuelos.
     */
    private final String URL = super.URL + "/flights";

    /**
     * Inserta en la Url el origen y destino, envía la petición al servidor para que sean recogidos
     * por RequestParam. El servidor tramita la petición y si es correcta y existen los vuelos con esos parámetros,
     * recoge los datos del cuerpo del Gson que el servidor nos envía como respuesta.
     *
     * @param origin      Origen en RequestParam.
     * @param destination Destino en RequestParam.
     * @return La lista de vuelos o error a capturar.
     * @throws Exception Si falla la petición.
     */
    public List<FlightDTO> getFlights(String origin, String destination) throws Exception {
        String body = connectionFlight.doGet(URL + "?ori=" + origin + "&des=" + destination);
        Gson gson = new Gson();
        FlightDTO[] flightArray = gson.fromJson(body, FlightDTO[].class);
        return Arrays.asList(flightArray);
    }

    /**
     * Busca un vuelo por PathVariable idCode y RequestParam date (en formato LocalDate).
     * Si el servidor encuentra el vuelo coincidente, lo recoge y lo transforma a objeto con gson.fromJson.
     *
     * @param idCode IdCode del vuelo en PathVariable.
     * @param date   Fecha del vuelo en RequestParam.
     * @return El vueloDTO o error a capturar.
     * @throws Exception Sí falla la petición al servidor.
     */
    public FlightDTO getFlight(String idCode, LocalDate date) throws Exception {
        String body = connectionFlight.doGet(URL + "/" + idCode + "?date=" + date);
        Gson gson = new Gson();
        return gson.fromJson(body, FlightDTO.class);
    }

    /**
     * Crea un Gson con el vuelo. Para ello y muy importante,
     * hay que poner la fecha al formato que acepta SpringBoot, de lo
     * contrario por defecto se pone una que no acepta. Una vez formateada la fecha del
     * vuelo y crear el cuerpo Gson que envía el vueloDTO.
     * Se recoge mensaje o error del servidor a capturar.
     *
     * @param flightDTO VueloDTO.
     * @return Respuesta del servidor o error a capturar.
     * @throws Exception Sí falla la petición al servidor.
     */
    public String createFlight(FlightDTO flightDTO) throws Exception {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .create();
        String jsonString = gson.toJson(flightDTO);
        return connectionFlight.doPost(jsonString, URL);
    }

    /**
     * Borra un vuelo por idCode de vuelo, es introducido como PathVariable de la URL.
     *
     * @param idCode IdCode del vuelo en PathVariable.
     * @return Respuesta del servidor o error a capturar.
     * @throws Exception Sí falla la petición.
     */
    public String deleteFlight(String idCode) throws Exception {
        return connectionFlight.doDelete(URL + "/" + idCode);
    }

    /**
     * Modifica un vuelo por idCode, mediante PathVariable idCode en el servidor.
     *
     * @param flightDTO vuelo a modificar.
     * @param idCode    idCode a buscar del vuelo.
     * @return respuesta del servidor en caso de OK.
     * @throws Exception tipo de error a capturar en el servicio de vuelo.
     */
    public String modifyFlight(FlightDTO flightDTO, String idCode) throws Exception {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .create();
        String jsonString = gson.toJson(flightDTO);
        return connectionFlight.doPut(jsonString, URL + "/" + idCode);
    }
}
