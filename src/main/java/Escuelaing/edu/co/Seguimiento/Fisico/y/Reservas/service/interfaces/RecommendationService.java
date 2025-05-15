package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;

import java.util.List;

public interface RecommendationService {
    List<Routine> recommendRoutineForUser(String userId);
}