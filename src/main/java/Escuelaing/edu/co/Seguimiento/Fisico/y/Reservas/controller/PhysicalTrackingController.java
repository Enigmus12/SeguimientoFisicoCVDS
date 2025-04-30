package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracking-service")
public class PhysicalTrackingController {

    @Autowired
    private PhysicalTrackingService trackingService;

    @PostMapping("/records")
    public PhysicalRecord registerPhysicalData(@RequestBody PhysicalRecord record) {
        return trackingService.createPhysicalRecord(record);
    }

    @GetMapping("/records/user/{userId}")
    public List<PhysicalRecord> getHistory(@PathVariable String userId) {
        return trackingService.getUserPhysicalHistory(userId);
    }
}
