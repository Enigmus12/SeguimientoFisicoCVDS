package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;

import java.util.List;

public interface RoutineService {
    Routine createRoutine(RoutineDTO routineDTO);
    Routine saveRoutine(Routine routine);
    List<Routine> getAllRoutines();
    Routine getRoutineById(String id);
    Routine updateRoutine(String id, RoutineDTO routineDTO);
    boolean deleteRoutine(String id);
    List<Routine> getRecommendations(String userId);
}