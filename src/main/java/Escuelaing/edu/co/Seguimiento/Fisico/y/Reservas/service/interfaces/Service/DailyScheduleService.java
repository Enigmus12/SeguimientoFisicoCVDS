package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.DailyScheduleDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.DailySchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyScheduleService {
    List<DailySchedule> findRescheduledByUserId(String userId);

    List<DailySchedule> generateDailySchedulesFromSemestral(String scheduleId);
    List<DailySchedule> generateDailySchedulesFromGroup(String scheduleGroupId);
    List<DailySchedule> findAll();
    Optional<DailySchedule> findById(String id);
    List<DailySchedule> findByScheduleId(String scheduleId);
    List<DailySchedule> findByScheduleGroupId(String scheduleGroupId);
    List<DailySchedule> addUserToScheduleByGroup(String scheduleId, String userId) throws Exception;
    List<DailySchedule> findIncompleteSchedules();
    DailySchedule removeUserFromScheduleById(String scheduleId, String userId) throws Exception;
    List<DailySchedule> removeUserFromScheduleByDate(LocalDate date, String userId) throws Exception;
    DailySchedule addUserToSchedule(String id, String userId) throws Exception;
}