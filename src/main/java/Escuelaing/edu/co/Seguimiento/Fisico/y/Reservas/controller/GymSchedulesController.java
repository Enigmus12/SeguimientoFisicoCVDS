package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gym-schedules")
public class GymSchedulesController {

    @Autowired
    private GymScheduleService gymScheduleService;

    @GetMapping()
    public ResponseEntity<List<GymSchedules>> getAllSchedules() {
        List<GymSchedules> schedules = gymScheduleService.findAll();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymSchedules> getScheduleById(@PathVariable String id) {
        Optional<GymSchedules> gymSchedule = gymScheduleService.findById(id);
        return gymSchedule.map(schedule -> new ResponseEntity<>(schedule, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<GymSchedules> createSchedule(
            @RequestBody GymSchedulesDTO gymSchedulesDTO) {
        GymSchedules createdSchedule = gymScheduleService.create(gymSchedulesDTO);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/capacity/{id}")
    public ResponseEntity<GymSchedules> updateCapacity(
            @PathVariable String id,
            @RequestParam Integer capacity) {
        GymSchedules updatedSchedule = gymScheduleService.updateCapacity(id, capacity);
        if (updatedSchedule != null) {
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}