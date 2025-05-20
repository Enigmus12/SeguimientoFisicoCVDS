package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.DailyScheduleDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.DailySchedule;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.DailyScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/daily-schedule")
public class DailyScheduleController {

    @Autowired
    private DailyScheduleService dailyScheduleService;

    @PostMapping("/generate/{scheduleId}")
    public ResponseEntity<?> generateDailySchedules(@PathVariable String scheduleId) {
        try {
            List<DailySchedule> generatedSchedules = dailyScheduleService.generateDailySchedulesFromSemestral(scheduleId);

            List<DailyScheduleDTO> dtoList = generatedSchedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(dtoList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al generar horarios diarios: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-schedule/{scheduleId}")
    public ResponseEntity<List<DailyScheduleDTO>> getDailySchedulesByScheduleId(@PathVariable String scheduleId) {
        List<DailySchedule> schedules = dailyScheduleService.findByScheduleId(scheduleId);

        List<DailyScheduleDTO> dtoList = schedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/by-group/{scheduleGroupId}")
    public ResponseEntity<List<DailyScheduleDTO>> getDailySchedulesByGroupId(@PathVariable String scheduleGroupId) {
        List<DailySchedule> schedules = dailyScheduleService.findByScheduleGroupId(scheduleGroupId);

        List<DailyScheduleDTO> dtoList = schedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/{scheduleGroupId}/add-user/{userId}")
    public ResponseEntity<?> addUserToAllSchedulesByGroup(@PathVariable String scheduleGroupId, @PathVariable String userId) {
        try {
            List<DailySchedule> updatedSchedules = dailyScheduleService.addUserToScheduleByGroup(scheduleGroupId, userId);
            List<DailyScheduleDTO> dtos = updatedSchedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para obtener horarios incompletos
    @GetMapping("/incomplete")
    public ResponseEntity<List<DailyScheduleDTO>> getIncompleteSchedules() {
        List<DailySchedule> schedules = dailyScheduleService.findIncompleteSchedules();

        List<DailyScheduleDTO> dtoList = schedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    private DailyScheduleDTO convertToDTO(DailySchedule schedule) {
        return new DailyScheduleDTO(
                schedule.getId(),
                schedule.getScheduleId(),
                schedule.getScheduleGroupId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDayOfWeek(),
                schedule.getCapacity(),
                schedule.isHoliday(),
                schedule.getHolidayDescription(),
                schedule.isRescheduled(),
                schedule.getOriginalDate(),
                schedule.getUsers(),
                schedule.getStatus()
        );
    }
}