package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/routine-service")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @PostMapping("/routines")
    public Routine createNewRoutine(@RequestBody Routine routine) {
        return routineService.createRoutine(routine);
    }

    @PostMapping("/routines/{routineId}/user/{userId}")
    public void assignRoutine(@PathVariable String routineId, @PathVariable String userId) {
        routineService.assignRoutineToUser(routineId, userId);
    }

    @GetMapping("/routines/available")
    public List<Routine> getAvailableRoutines() {
        return new ArrayList<>();
    }
}
