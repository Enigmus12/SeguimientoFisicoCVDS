package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.RoutineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/routine-service")
@CrossOrigin(origins = "*")
public class RoutineController {

  @Autowired
  private RoutineService routineService;

  @PostMapping("/create")
  @PreAuthorize("hasRole('Coache')")
  public ResponseEntity<Routine> createRoutine(
    @RequestBody RoutineDTO routineDTO
  ) {
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
  public ResponseEntity<Routine> updateRoutine(
    @PathVariable String id,
    @RequestBody RoutineDTO routineDTO
  ) {
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

  @GetMapping("/recommendations")
  public ResponseEntity<List<Routine>> recommendRoutine(
    Authentication authentication
  ) {
    try {
      String username = authentication.getName();
      System.out.println("Username: " + username);
      authentication
        .getAuthorities()
        .forEach(auth ->
          System.out.println("Rol/autoridad: " + auth.getAuthority())
        );
      List<Routine> recommended = routineService.getRecommendations(username);
      return ResponseEntity.ok(recommended);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/rutina/{username}")
  public ResponseEntity<Routine> createRoutineForUser(
    @PathVariable String username,
    @RequestBody String routineId
  ) {
    // Quita comillas si existen
    routineId = routineId.replace("\"", "");
    Routine routine = routineService.getRoutineById(routineId);
    if (routine == null) {
      System.out.println("Routine with ID " + routineId + " not found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    try {
      routineService.createRoutineForUser(username, routine);
      System.out.println(
        "Routine with ID " + routineId + " created for user " + username
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(routine);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/user/{username}/routines")
  public ResponseEntity<List<Routine>> getUserRoutines(
    @PathVariable String username
  ) {
    try {
      List<Routine> userRoutines = routineService.getUserRoutines(username);
      return ResponseEntity.ok(userRoutines);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/user/{username}/routine/{routineId}")
  public ResponseEntity<Routine> getUserRoutineById(
    @PathVariable String username,
    @PathVariable String routineId
  ) {
    try {
      Routine userRoutine = routineService.getUserRoutineById(
        username,
        routineId
      );
      if (userRoutine != null) {
        return ResponseEntity.ok(userRoutine);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/user/{username}/routine/{routineId}/update")
  //@PreAuthorize("hasRole('Coache')")
  public ResponseEntity<Routine> updateUserRoutine(
    @PathVariable String username,
    @PathVariable String routineId,
    @RequestBody RoutineDTO routineDTO
  ) {
    try {
      System.out.println(
        "Updating routine for user: " + username + ", Routine ID: " + routineId
      );
      String result = routineService.updateUserRoutine(
        username,
        routineId,
        routineDTO
      );
      System.out.println(result);
      routineService.getUserRoutines(username);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
