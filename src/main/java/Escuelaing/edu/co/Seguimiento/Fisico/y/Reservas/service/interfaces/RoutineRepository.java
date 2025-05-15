package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoutineRepository extends MongoRepository<Routine, String> {
    List<Routine> findByObjective(String objective);
}