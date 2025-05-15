package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymReservationMongoRepository extends MongoRepository<GymReservation, String> {
    List<GymReservation> findByUserId(String userId);
    boolean existsByUserIdAndDayOfWeekAndStartTimeAndEndTime(String userId, String dayOfWeek, String startTime, String endTime);
}
