package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routine-service")
@CrossOrigin(origins = "*")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<Routine> createRoutine(@RequestBody RoutineDTO routineDTO) {
        Routine createdRoutine = routineService.createRoutine(routineDTO);
        return new ResponseEntity<>(createdRoutine, HttpStatus.CREATED);
    }

    @PostMapping("/auto/create")
    public ResponseEntity<Routine> createRoutineWithToken(@RequestBody RoutineDTO routineDTO,
                                                          @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            jwtUtil.extractUserId(token); // Valida el token

            Routine routine = new Routine(routineDTO);
            Routine savedRoutine = routineService.saveRoutine(routine);

            return new ResponseEntity<>(savedRoutine, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/routines")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        return new ResponseEntity<>(routineService.getAllRoutines(), HttpStatus.OK);
    }

    @GetMapping("/routine/{id}")
    public ResponseEntity<Routine> getRoutineById(@PathVariable String id) {
        Routine routine = routineService.getRoutineById(id);
        return routine != null ? new ResponseEntity<>(routine, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable String id, @RequestBody RoutineDTO routineDTO) {
        Routine updatedRoutine = routineService.updateRoutine(id, routineDTO);
        return updatedRoutine != null ? new ResponseEntity<>(updatedRoutine, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        boolean deleted = routineService.deleteRoutine(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Routine>> recommendRoutine(Authentication authentication) {
        try {
            String username = authentication.getName();
            System.out.println("Usuario autenticado: " + username);

            List<Routine> recommended = routineService.getRecommendations(username);
            return ResponseEntity.ok(recommended);
        } catch (Exception e) {
            System.err.println("Error al buscar recomendaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}