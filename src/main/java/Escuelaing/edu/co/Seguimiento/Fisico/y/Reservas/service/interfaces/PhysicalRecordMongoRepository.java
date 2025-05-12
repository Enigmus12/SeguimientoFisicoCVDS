package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalRecordMongoRepository extends MongoRepository<PhysicalRecord, String> {
    List<PhysicalRecord> findByUserName(String userName);
    List<PhysicalRecord> findByUserId(String userId);
}