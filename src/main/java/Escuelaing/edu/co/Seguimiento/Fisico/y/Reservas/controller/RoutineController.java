package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routine-service")
@CrossOrigin(origins = "*")
public class RoutineController {

    @Autowired
    private RoutineService routineService;


    @PostMapping("/create")
    public ResponseEntity<Routine> createRoutine(@RequestBody RoutineDTO routineDTO) {
        Routine createdRoutine = routineService.createRoutine(routineDTO);
        return new ResponseEntity<>(createdRoutine, HttpStatus.CREATED);
    }

    @GetMapping("/routines")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.getAllRoutines();
        return new ResponseEntity<>(routines, HttpStatus.OK);
    }

    @GetMapping("/routine/{id}")
    public ResponseEntity<Routine> getRoutineById(@PathVariable String id) {
        Routine routine = routineService.getRoutineById(id);
        if (routine != null) {
            return new ResponseEntity<>(routine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable String id, @RequestBody RoutineDTO routineDTO) {
        Routine updatedRoutine = routineService.updateRoutine(id, routineDTO);
        if (updatedRoutine != null) {
            return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        boolean deleted = routineService.deleteRoutine(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}