package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoutineServiceImpl implements RoutineService {
    @Override
    public Routine createRoutine(Routine routine) {
        // Basic implementation
        return routine;
    }

    @Override
    public Routine getRoutine(String id) {
        // Basic implementation
        return new Routine();
    }

    @Override
    public List<Routine> getUserRoutines(String userId) {
        // Basic implementation
        return new ArrayList<>();
    }

    @Override
    public void assignRoutineToUser(String routineId, String userId) {
        // Basic implementation
    }

    @Override
    public void updateRoutine(Routine routine) {
        // Basic implementation
    }
}

