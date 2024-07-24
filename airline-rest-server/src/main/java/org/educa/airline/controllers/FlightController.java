package org.educa.airline.controllers;

import jakarta.validation.Valid;
import org.educa.airline.dto.FlightDTO;
import org.educa.airline.entity.Flight;
import org.educa.airline.mappers.FlightMapper;
import org.educa.airline.services.inservice.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Controlador asociado a ruta /flights
 */
@RestController
@RequestMapping(path = "/flights")
public class FlightController {

    /**
     * Clases a inyectar en el constructor.
     */
    private final FlightService serviceFlight;
    private final FlightMapper flightMapper;

    /**
     * Se inyectan las clases en el constructor para poder acceder a ellas.
     *
     * @param flightService capa del servicio de vuelos.
     * @param flightMapper  para transformar DTO a entity y viceversa.
     */
    @Autowired
    public FlightController(FlightService flightService, FlightMapper flightMapper) {
        this.serviceFlight = flightService;
        this.flightMapper = flightMapper;
    }

    /**
     * Necesita 2 parámetros en la URL = ori y des, que son los 2 campos que el cliente envía.
     * Busca un vuelo por estos 2 campos, si lo encuentra lo envía al cliente con la respuesta 200,
     * si no lo encuentra envía un 404 notFound.
     *
     * @param origin      RequestParam.
     * @param destination RequestParam.
     * @return httpStatus 200 con la lista vuelosDTO.
     * httpStatus 404 notFound.
     */

    @GetMapping()
    public ResponseEntity<List<FlightDTO>> getFlights(@RequestParam(required = true, name = "ori") String origin,
                                                      @RequestParam(required = true, name = "des") String destination) {

        List<Flight> flights = this.serviceFlight.list(origin, destination);
        if (!flights.isEmpty()) {
            List<FlightDTO> flightsDTO = this.flightMapper.flightsToTDO(flights);
            return ResponseEntity.ok(flightsDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca un vuelo por PathVariable idCode del vuelo para localizar el atributo único y también se requiere
     * la fecha como RequestParam. Si el servidor encuentra un vuelo con estos criterios de búsqueda,
     * devuelve el vuelo, si no captura envía 404 notFound.
     *
     * @param idCode     PathVariable.
     * @param dateString RequestParam.
     * @return httpStatus 200 u vueloDTO.
     * httpStatus 404 notFound.
     */
    // localhost:8080/flights/1?date=12/01/2024
    @GetMapping("/{idCode}")
    public ResponseEntity<FlightDTO> getFightByIdDate(@PathVariable("idCode") String idCode,
                                                      @RequestParam(required = false, name = "date") LocalDate dateString) {

        Date date = java.sql.Date.valueOf(dateString);
        Flight flight = this.serviceFlight.getFlightByIdCodeDate(idCode, date);
        if (flight != null) {
            return ResponseEntity.ok(this.flightMapper.flightToDTO(flight));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un vuelo. En primer lugar, comprueba que los campos del vuelo no estén vacíos.
     * Si alguno de los campos está vacío, devuelve como respuesta badRequest.
     * La clase BindingResult recoge el error en formato json y lo transforma en una lista de String,
     * hay que extraer la cabecera "errors".
     * El mensaje que se devuelve es el que se ha manejado en la etiqueta del atributo en FlightDTO.
     *
     * @param flightDTO RequestBody.
     * @return httpStatus 201 si se crea el vuelo.
     * httpStatus 400 badRequest si hay algún campo vacío.
     * httpStatus 409 vuelo ya existe.
     */

    @PostMapping()
    public ResponseEntity<String> addFlight(@RequestBody @Valid FlightDTO flightDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        boolean insert = this.serviceFlight.addFlight(this.flightMapper.flightDTOtoEntity(flightDTO));
        if (insert) {
            return ResponseEntity.status(201).body("Vuelo creado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    /**
     * Borra un vuelo por idCode, clave única del HashMap de vuelos. Si existe un vuelo con ese idCode
     * se borra y envía httpStatus 200, y si no devuelve un httpStatus 404.
     *
     * @param flightIdCode PathVariable.
     * @return httpStatus 200 y mensaje ok.
     * httpStatus 404 con notFound.
     */

    @DeleteMapping("/{flightIdCode}")
    public ResponseEntity<String> deleteFlight(@PathVariable("flightIdCode") String flightIdCode) {

        boolean delete = this.serviceFlight.deleteFlight(flightIdCode);
        if (delete) {
            return ResponseEntity.ok("Vuelo " + flightIdCode + " borrado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Modifica un vuelo por idCode, clave única en el HashMap de vuelo. Para ello comprueba que los
     * datos del vuelo sean correctos. Después verifica su existencia y si se encuentra en el repositorio
     * se modifica con los nuevos datos del body.
     *
     * @param flightIdCode  idCode a buscar del vuelo.
     * @param flightDTO     vuelo enviado por el cliente con los nuevos datos.
     * @param bindingResult clase que comprueba que los datos del vuelo sean correctos.
     * @return httpStatus 200 si se modifica el vuelo.
     * httpStatus 400 badRequest si hay algún campo vacío.
     * httpStatus 404 vuelo no existe.
     */
    @PutMapping("/{flightIdCode}")
    public ResponseEntity<String> updateFlight(@PathVariable("flightIdCode") String flightIdCode,
                                               @RequestBody @Valid FlightDTO flightDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        boolean update = this.serviceFlight.updateFlight(flightIdCode, this.flightMapper.flightDTOtoEntity(flightDTO));
        if (update) {
            return ResponseEntity.status(200).body("Vuelo actualizado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
