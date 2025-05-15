package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GymScheduleMongoRepository extends MongoRepository<GymSchedules, String> {

    @Query("{'dayOfWeek': ?0, 'startTime': ?1, 'endTime': ?2, '$or': [" +
            "{'startDate': {$lte: ?4}, 'endDate': {$gte: ?3}}, " +
            "{'startDate': {$lte: ?3}, 'endDate': {$gte: ?3}}, " +
            "{'startDate': {$lte: ?4}, 'endDate': {$gte: ?4}}, " +
            "{'startDate': {$gte: ?3}, 'endDate': {$lte: ?4}}" +
            "]}")
    List<GymSchedules> findOverlappingSchedules(String dayOfWeek, String startTime, String endTime,
                                                String startDate, String endDate);

    List<GymSchedules> findByScheduleGroupId(String scheduleGroupId);
}