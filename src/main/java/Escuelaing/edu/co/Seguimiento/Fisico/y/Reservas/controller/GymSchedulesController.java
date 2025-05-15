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

    @PostMapping("/create-semestral")
    public ResponseEntity<?> createSemestralSchedules(@RequestBody GymSchedulesDTO gymSchedulesDTO) {
        try {
            List<GymSchedules> createdSchedules = gymScheduleService.createSemestralSchedules(gymSchedulesDTO);
            return new ResponseEntity<>(createdSchedules, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @PutMapping("/group-capacity/{scheduleGroupId}")
    public ResponseEntity<List<GymSchedules>> updateGroupCapacity(
            @PathVariable String scheduleGroupId,
            @RequestParam Integer capacity) {
        List<GymSchedules> updatedSchedules = gymScheduleService.updateGroupCapacity(scheduleGroupId, capacity);
        if (!updatedSchedules.isEmpty()) {
            return new ResponseEntity<>(updatedSchedules, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-all-capacity")
    public ResponseEntity<List<GymSchedules>> updateAllSchedulesCapacity(
            @RequestParam Integer capacity) {
        List<GymSchedules> updatedSchedules = gymScheduleService.updateAllSchedulesCapacity(capacity);
        if (!updatedSchedules.isEmpty()) {
            return new ResponseEntity<>(updatedSchedules, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}