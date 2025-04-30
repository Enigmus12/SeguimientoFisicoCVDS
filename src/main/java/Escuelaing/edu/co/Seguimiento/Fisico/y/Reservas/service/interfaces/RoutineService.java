package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;

import java.util.List;

public interface RoutineService {
    Routine createRoutine(Routine routine);
    Routine getRoutine(String id);
    List<Routine> getUserRoutines(String userId);
    void assignRoutineToUser(String routineId, String userId);
    void updateRoutine(Routine routine);
}

