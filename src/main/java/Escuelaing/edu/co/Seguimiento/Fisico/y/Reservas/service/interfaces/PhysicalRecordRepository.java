package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PhysicalRecordRepository extends MongoRepository<PhysicalRecord, String> {
    Optional<PhysicalRecord> findByUserId(Integer userId);
}