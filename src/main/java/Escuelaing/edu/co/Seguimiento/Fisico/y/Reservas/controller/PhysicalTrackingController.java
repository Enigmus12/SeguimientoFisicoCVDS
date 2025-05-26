package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.PhysicalTrackingService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/tracking-service")
@CrossOrigin(origins = "*") // Para permitir peticiones desde el frontend
public class PhysicalTrackingController {

    @Autowired
    private PhysicalTrackingService trackingService;

    @Autowired
    private JwtUtil jwtUtil;

    // Crear un nuevo registro físico
    @PostMapping("/records")
    public ResponseEntity<PhysicalRecord> registerPhysicalData(@RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord record = new PhysicalRecord(recordDTO);
        PhysicalRecord savedRecord = trackingService.createPhysicalRecord(record);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    @PostMapping("/records/auto")
    public ResponseEntity<?> registerAutoPhysicalData(
            @RequestBody PhysicalRecordDTO simplifiedDTO,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Extraer el token del header
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token); // Ahora el token contiene el userId

            // Llamar al endpoint existente para obtener los datos del usuario
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<UserResponseDTO> userResponse = restTemplate.exchange(
                    "https://seguimientofisicoreservas-c0dhhcgyb0b9atcc.eastus2-01.azurewebsites.net/user-service/users/" + userId, // URL para buscar por userId
                    HttpMethod.GET,
                    entity,
                    UserResponseDTO.class
            );

            if (userResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Error al obtener datos del usuario",
                        userResponse.getStatusCode());
            }

            UserResponseDTO userData = userResponse.getBody();

            // Crear el registro físico completo
            PhysicalRecord record = new PhysicalRecord();
            record.setUserId(userId); // Ahora usamos el userId del token
            record.setUserName(userData.getUserName()); // El userName ahora es un atributo del usuario
            record.setNumberId(userData.getNumberId());
            record.setRegistrationDate(new Date());
            record.setWeight(simplifiedDTO.getWeight());
            record.setBodyMeasurements(simplifiedDTO.getBodyMeasurements());
            record.setPhysicalGoal(simplifiedDTO.getPhysicalGoal());
            record.setRole(userData.getRole());
            // Dejar vacíos observations y activeRoutine
            record.setObservations("");
            record.setActiveRoutine("");

            // Guardar el registro
            PhysicalRecord savedRecord = trackingService.createPhysicalRecord(record);
            return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);

        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Usuario no encontrado: " + e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener Todos los Registros
    @GetMapping("/records")
    public List<PhysicalRecord> records(){
        return trackingService.getAllrecords();
    }

    // Obtener el historial de un usuario por userName
    @GetMapping("/records/user/{username}")
    public ResponseEntity<List<PhysicalRecord>> getHistoryByUsername(@PathVariable String username) {
        List<PhysicalRecord> records = trackingService.getUserPhysicalHistory(username);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Obtener el historial de un usuario por userId
    @GetMapping("/records/userId/{userId}")
    public ResponseEntity<List<PhysicalRecord>> getHistoryByUserId(@PathVariable String userId) {
        List<PhysicalRecord> records = trackingService.getUserPhysicalHistoryByUserId(userId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Obtener un registro específico por ID
    @GetMapping("/records/{id}")
    public ResponseEntity<PhysicalRecord> getRecord(@PathVariable String id) {
        PhysicalRecord record = trackingService.getPhysicalRecord(id);
        if (record != null) {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<Void> updateRecord(@PathVariable String id, @RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord existingRecord = trackingService.getPhysicalRecord(id);
        if (existingRecord != null) {
            // Solo actualizamos los campos observations y activeRoutine, ignorando el resto
            existingRecord.setObservations(recordDTO.getObservations());
            existingRecord.setActiveRoutine(recordDTO.getActiveRoutine());

            trackingService.updatePhysicalRecord(existingRecord);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
