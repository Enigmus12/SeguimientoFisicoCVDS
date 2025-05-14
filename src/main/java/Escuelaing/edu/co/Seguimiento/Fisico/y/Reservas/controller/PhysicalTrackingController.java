package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
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
@CrossOrigin(origins = "*")
public class PhysicalTrackingController {

    @Autowired
    private PhysicalTrackingService trackingService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/records")
    public ResponseEntity<PhysicalRecord> registerPhysicalData(@RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord saved = trackingService.savePhysicalRecord(recordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/records/auto")
    public ResponseEntity<?> registerAutoPhysicalData(
            @RequestBody PhysicalRecordDTO simplifiedDTO,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUserId(token);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                    "http://localhost:8080/user-service/users/" + username,
                    HttpMethod.GET,
                    entity,
                    UserResponseDTO.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Error al obtener datos del usuario.");
            }

            UserResponseDTO userData = response.getBody();

            PhysicalRecord record = new PhysicalRecord();
            record.setUserName(userData.getUserId());
            record.setUserId(userData.getNumberId());
            record.setRole(userData.getRole());
            record.setRegistrationDate(new Date());
            record.setWeight(simplifiedDTO.getWeight());
            record.setBodyMeasurements(simplifiedDTO.getBodyMeasurements());
            record.setPhysicalGoal(simplifiedDTO.getPhysicalGoal());
            record.setObservations("");
            record.setActiveRoutine("");

            PhysicalRecord saved = trackingService.createPhysicalRecord(record);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @GetMapping("/records")
    public ResponseEntity<List<PhysicalRecord>> getAllRecords() {
        return ResponseEntity.ok(trackingService.getAllRecords());
    }

    @GetMapping("/records/user/{username}")
    public ResponseEntity<PhysicalRecord> getHistoryByUser(@PathVariable String username) {
        PhysicalRecord record = trackingService.getUserPhysicalHistory(username);
        return record != null ?
            ResponseEntity.ok(record) :
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping("/records/{id}")
    public ResponseEntity<PhysicalRecord> getRecord(@PathVariable String id) {
        PhysicalRecord record = trackingService.getPhysicalRecord(id);
        return record != null ?
                ResponseEntity.ok(record) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<Void> updateRecord(@PathVariable String id,
                                             @RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord existing = trackingService.getPhysicalRecord(id);
        if (existing != null) {
            existing.setObservations(recordDTO.getObservations());
            existing.setActiveRoutine(recordDTO.getActiveRoutine());
            trackingService.updatePhysicalRecord(existing);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}