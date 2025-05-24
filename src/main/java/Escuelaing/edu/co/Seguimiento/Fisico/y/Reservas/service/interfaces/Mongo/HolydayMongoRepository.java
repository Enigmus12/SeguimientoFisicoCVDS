package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolydayMongoRepository extends MongoRepository<Holiday, String> {
    @Query("{'date': ?0}")
    Optional<Holiday> findByDate(LocalDate date);
}