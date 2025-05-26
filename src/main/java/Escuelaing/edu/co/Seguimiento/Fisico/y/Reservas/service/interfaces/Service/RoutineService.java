package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import java.util.List;

public interface RoutineService {
  Routine createRoutine(RoutineDTO routineDTO);
  List<Routine> getAllRoutines();
  Routine getRoutineById(String id);
  Routine updateRoutine(String id, RoutineDTO routineDTO);
  boolean deleteRoutine(String id);
  List<Routine> getRecommendations(String username);
  void createRoutineForUser(String username, Routine routine);
  List<Routine> getUserRoutines(String username);
  Routine getUserRoutineById(String username, String routineId);
  String updateUserRoutine(
    String username,
    String routineId,
    RoutineDTO routineDTO
  );
}
