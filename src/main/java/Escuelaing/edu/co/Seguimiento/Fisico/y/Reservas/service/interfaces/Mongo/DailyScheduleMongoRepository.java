package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.DailySchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyScheduleMongoRepository extends MongoRepository<DailySchedule, String> {

    List<DailySchedule> findByScheduleId(String scheduleId);
    List<DailySchedule> findByScheduleGroupId(String scheduleGroupId);
    List<DailySchedule> findByStatus(String status);
    List<DailySchedule> findByDate(LocalDate date);
    List<DailySchedule> findByDateBetweenAndUsersContaining(LocalDate startDate, LocalDate endDate, String userId);
}