package org.educa.airline.controllers;

import jakarta.validation.Valid;
import org.educa.airline.dto.PassengerDTO;
import org.educa.airline.entity.Passenger;
import org.educa.airline.mappers.PassengerMapper;
import org.educa.airline.services.inservice.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * El controlador de pasajeros tiene un RequestMapping /flights/{idCode}/passengers, de esta forma
 * cuando se realicen peticiones desde el cliente cogeremos esta ruta para acceder a los métodos.
 */
@RestController
@RequestMapping("/flights/{idCode}/passengers")
public class PassengerController {
    /**
     * Clases a inyectar en el constructor.
     */
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    /**
     * Se inyectan las clases en el constructor para poder acceder a ellas.
     *
     * @param passengerService capa de servicio de pasajeros.
     * @param passengerMapper  para convertir DTO a entity y viceversa.
     */
    @Autowired
    public PassengerController(PassengerService passengerService, PassengerMapper passengerMapper) {
        this.passengerService = passengerService;
        this.passengerMapper = passengerMapper;
    }

    /**
     * Asocia pasajeros a vuelos. Para ello antes de insertar un pasajero en un vuelo,
     * se comprueban diferentes situaciones. Primero que el cuerpo del pasajero esté completo, si no
     * devuelve error 400 (BadRequest). Segundo que haya vuelos existentes con el idCode del pasajero, si no devuelve
     * error 404 (NotFound). Y por último si existe el pasajero en el vuelo, devuelve 409 (Conflict).
     * Si se inserta, se devuelve 201 (ok).
     *
     * @param passengerDTO  pasajeroDTO que envía el cliente.
     * @param bindingResult clase que comprueba que el cuerpo del pasajero esté completo.
     *                      Se ayuda de las anotaciones en la clase passengerDTO.
     * @return httpStatus 201 (creado).
     * httpStatus 400 (BadRequest).
     * httpStatus 404 (NotFound).
     * httpStatus 409 (Conflict).
     */
    @PostMapping
    public ResponseEntity<String> addPassenger(@RequestBody @Valid PassengerDTO passengerDTO, BindingResult bindingResult) {
        // Si falta algún atributo del pasajero, indica cúal o cúales faltan mediante el envío un mensaje.
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        // Sí no hay vuelos con el idCodeVuelo del pasajero significa que se ha borrado
        // del repositorio de vuelos, por lo que se borrará del repositorio de pasajeros
        // en caso de que exista. Esta comprobación se realizará en el resto de métodos.
        if (this.passengerService.getFlightIdCode(passengerDTO.getFlightIdCode()) == null) {
            this.passengerService.deleteFlight(passengerDTO.getFlightIdCode());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si el pasajero no está en el vuelo y se inserta.
        if (this.passengerService.addPassenger(passengerMapper.passengerToEntity(passengerDTO))) {
            return ResponseEntity.status(201).body("Pasajero:" + passengerDTO.getName() +
                    ", nif:" + passengerDTO.getNif() + " insertado correctamente");
        } else {
            // Si el pasajero ya está en el vuelo.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Consulta que un pasajero vaya en un vuelo. Primero comprueba que el vuelo exista, en caso de no existir,
     * devuelve 404 notfound. Si el pasajero no existe en el vuelo devuelve httpStatus 409 (Conflict),
     * y si existe, httpStatus 200 con el pasajero.
     *
     * @param idCode idCode de vuelo.
     * @param nif    identificador del pasajero.
     * @return httpStatus 404 (NotFound).
     * httpStatus 409 (Conflict).
     * httpStatus 200 (Ok).
     */
    @GetMapping("/{nif}")
    public ResponseEntity<PassengerDTO> getPassenger(@PathVariable("idCode") String idCode, @PathVariable("nif") String nif) {

        if (this.passengerService.getFlightIdCode(idCode) == null) {
            this.passengerService.deleteFlight(idCode);
            return ResponseEntity.notFound().build();
        }

        Passenger passenger = this.passengerService.getPassenger(idCode, nif);
        if (passenger != null) {
            PassengerDTO passengerDTO = this.passengerMapper.passengerToDTO(passenger);
            return ResponseEntity.ok(passengerDTO);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    /**
     * Busca todos los pasajeros en un vuelo por idCode. Si el vuelo no existe, devuelve httpStatus 404,
     * si el vuelo existe, pero no hay pasajeros httpStatus 409 y si hay pasajeros en el vuelo,
     * httpStatus 200 y una lista de pasajeroDTOs a enviar al cliente.
     *
     * @param idCode clave del vuelo a buscar.
     * @return httpStatus 404 (NotFound).
     * httpStatus 409 (Conflict).
     * httpStatus 200 (Ok).
     */
    @GetMapping()
    public ResponseEntity<List<PassengerDTO>> getPassengersFlight(@PathVariable("idCode") String idCode) {

        if (this.passengerService.getFlightIdCode(idCode) == null) {
            this.passengerService.deleteFlight(idCode);
            return ResponseEntity.notFound().build();
        }

        List<Passenger> passengers = this.passengerService.getPassengers(idCode);
        if (!passengers.isEmpty()) {
            List<PassengerDTO> passengersDTO = this.passengerMapper.passengersToDTOs(passengers);
            return ResponseEntity.ok(passengersDTO);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    /**
     * Elimina un pasajero en un vuelo. Buscar el vuelo mediante idCode PathVariable, si no existe 404 httpStatus
     * y si existe el vuelo, busca el pasajero con nif PathVariable. Si no existe el pasajero httpStatus 409,
     * y si existe httpStatus 200 y el pasajero.
     *
     * @param idCode clave del vuelo buscar.
     * @param nif    nif del pasajero a buscar en el vuelo.
     * @return httpStatus 404 (NotFound).
     * httpStatus 409 (Conflict).
     * httpStatus 200 (Ok) y el pasajero.
     */
    @DeleteMapping("/{nif}")
    public ResponseEntity<String> deletePassenger(@PathVariable("idCode") String idCode, @PathVariable("nif") String nif) {

        if (this.passengerService.getFlightIdCode(idCode) == null) {
            this.passengerService.deleteFlight(idCode);
            return ResponseEntity.notFound().build();
        }

        boolean delete = this.passengerService.deletePassenger(idCode, nif);
        if (delete) {
            return ResponseEntity.ok("Pasajero:" + nif + " borrado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    /**
     * Modifica un pasajero en un vuelo concreto. Primero verifica que el pasajero
     * tiene completos y correctos todos sus atributos, si no httpStatus 400.
     * Busca el vuelo donde se ubica el pasajero por idCode, si no existe httpStatus 404,
     * si el vuelo existe, pero el pasajero no httpStatus 409 y si existe y se modifica httpStatus 200.
     *
     * @param nif           nif del pasajero a buscar en el vuelo.
     * @param passengerDTO  nuevos datos del pasajero a modificar.
     * @param bindingResult clase que comprueba por las etiquetas que los atributos del pasajero sean correctos.
     * @return httpStatus 404 (NotFound).
     * httpStatus 400 (BadRequest).
     * httpsStatus 409(Conflict).
     * httpsStatus 200 (Ok).
     */
    @PutMapping("/{nif}")
    public ResponseEntity<String> updatePassenger(@PathVariable("nif") String nif,
                                                  @RequestBody @Valid PassengerDTO passengerDTO,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String errorMessage = "Los datos de la solicitud son inválidos:\n" + String.join("\n", errors);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (this.passengerService.getFlightIdCode(passengerDTO.getFlightIdCode()) == null) {
            this.passengerService.deleteFlight(passengerDTO.getFlightIdCode());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Hay que verificar su existencia, importante.
        if (this.passengerService.existsPassenger(passengerDTO.getFlightIdCode(), nif)) {
            this.passengerService.updatePassenger(nif, this.passengerMapper.passengerToEntity(passengerDTO));
            return ResponseEntity.ok("Pasajero:" + nif + " modificado correctamente en vuelo:" + passengerDTO.getFlightIdCode());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
