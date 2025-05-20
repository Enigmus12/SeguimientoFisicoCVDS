package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineMongoRepository extends MongoRepository<Routine, String> {
}