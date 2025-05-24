package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface GymScheduleMongoRepository extends MongoRepository<GymSchedules, String> {

    // Buscar por scheduleGroupId
    List<GymSchedules> findByScheduleGroupId(String scheduleGroupId);

    // Método para buscar horarios que se solapen con uno nuevo
    // Usando directamente LocalDate y LocalTime
    @Query(value = "{" +
            "'dayOfWeek': ?0, " +
            "$or: [" +
            "  { $and: [" +
            "    { 'startTime': { $lte: ?1 } }, " +
            "    { 'endTime': { $gt: ?1 } }" +
            "  ]}, " +
            "  { $and: [" +
            "    { 'startTime': { $lt: ?2 } }, " +
            "    { 'endTime': { $gte: ?2 } }" +
            "  ]}, " +
            "  { $and: [" +
            "    { 'startTime': { $gte: ?1 } }, " +
            "    { 'endTime': { $lte: ?2 } }" +
            "  ]}" +
            "], " +
            "$or: [" +
            "  { $and: [" +
            "    { 'startDate': { $lte: ?3 } }, " +
            "    { 'endDate': { $gt: ?3 } }" +
            "  ]}, " +
            "  { $and: [" +
            "    { 'startDate': { $lt: ?4 } }, " +
            "    { 'endDate': { $gte: ?4 } }" +
            "  ]}, " +
            "  { $and: [" +
            "    { 'startDate': { $gte: ?3 } }, " +
            "    { 'endDate': { $lte: ?4 } }" +
            "  ]}" +
            "]" +
            "}")
    List<GymSchedules> findOverlappingSchedules(
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            LocalDate startDate,
            LocalDate endDate
    );

    // Método alternativo que no depende de la consulta MongoDB directa
    // Esto puede ser más confiable porque Spring Data convertirá correctamente los tipos
    List<GymSchedules> findByDayOfWeekAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String dayOfWeek,
            LocalDate endDate,
            LocalDate startDate
    );
}