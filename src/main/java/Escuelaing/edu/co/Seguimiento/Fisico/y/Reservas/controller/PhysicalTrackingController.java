package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tracking-service")
@CrossOrigin(origins = "*") // Para permitir peticiones desde el frontend
public class PhysicalTrackingController {

    @Autowired
    private PhysicalTrackingService trackingService;

    // Crear un nuevo registro físico
    @PostMapping("/records")
    public ResponseEntity<PhysicalRecord> registerPhysicalData(@RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord record = new PhysicalRecord(recordDTO);
        PhysicalRecord savedRecord = trackingService.createPhysicalRecord(record);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    // Obtener el historial de un usuario
    @GetMapping("/records/user/{username}")
    public ResponseEntity<List<PhysicalRecord>> getHistory(@PathVariable String username) {
        List<PhysicalRecord> records = trackingService.getUserPhysicalHistory(username);
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

    // Actualizar un registro existente
    @PutMapping("/records/{id}")
    public ResponseEntity<Void> updateRecord(@PathVariable String id, @RequestBody PhysicalRecordDTO recordDTO) {
        PhysicalRecord existingRecord = trackingService.getPhysicalRecord(id);
        if (existingRecord != null) {
            PhysicalRecord updatedRecord = new PhysicalRecord(recordDTO);
            updatedRecord.setId(id);
            trackingService.updatePhysicalRecord(updatedRecord);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}