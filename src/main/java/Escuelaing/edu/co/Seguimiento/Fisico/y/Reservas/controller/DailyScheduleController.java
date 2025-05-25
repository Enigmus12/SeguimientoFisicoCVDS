package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.DailyScheduleDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.DailySchedule;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.DailyScheduleService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/daily-schedule")
@CrossOrigin(origins = "*")
public class DailyScheduleController {

    @Autowired
    private DailyScheduleService dailyScheduleService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/generate/{scheduleGroupId}")
    public ResponseEntity<?> generateDailySchedulesByGroup(@PathVariable String scheduleGroupId) {
        try {
            List<DailySchedule> generatedSchedules = dailyScheduleService.generateDailySchedulesFromGroup(scheduleGroupId);

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

    @PostMapping("/{scheduleGroupId}/add-user/{userId}")
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

    // Endpoint para añadir usuario por ID específico del horario
    @PostMapping("/{id}/add-user")
    public ResponseEntity<?> reserveToSchedule(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            // Extraer token
            String jwtToken = token.replace("Bearer ", "");

            // Extraer userId del token
            String userId = jwtUtil.extractUserId(jwtToken);

            // Añadir usuario al horario
            DailySchedule updatedSchedule = dailyScheduleService.addUserToSchedule(id, userId);

            // Convertir a DTO y devolver la respuesta
            DailyScheduleDTO dto = convertToDTO(updatedSchedule);

            return new ResponseEntity<>(dto, HttpStatus.OK);
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

    @DeleteMapping("/remove-user/{id}")
    public ResponseEntity<?> removeUserFromScheduleById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {

        try {
            // Extraer token
            String jwtToken = token.replace("Bearer ", "");

            // Extraer userId del token
            String userId = jwtUtil.extractUserId(jwtToken);

            // Eliminar usuario del horario
            DailySchedule updatedSchedule = dailyScheduleService.removeUserFromScheduleById(id, userId);

            // Convertir a DTO y devolver la respuesta
            DailyScheduleDTO dto = convertToDTO(updatedSchedule);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Nuevo endpoint para eliminar usuario por fecha
    @DeleteMapping("/remove-user/by-date/{date}")
    public ResponseEntity<?> removeUserFromScheduleByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestHeader("Authorization") String token) {

        try {
            // Extraer token
            String jwtToken = token.replace("Bearer ", "");

            // Extraer userId del token
            String userId = jwtUtil.extractUserId(jwtToken);

            // Eliminar usuario de los horarios en esa fecha
            List<DailySchedule> updatedSchedules = dailyScheduleService.removeUserFromScheduleByDate(date, userId);

            // Convertir a DTO y devolver la respuesta
            List<DailyScheduleDTO> dtoList = updatedSchedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/holidays/by-group/{scheduleGroupId}")
    public ResponseEntity<?> getHolidaySchedulesByGroup(@PathVariable String scheduleGroupId) {
        try {
            List<DailySchedule> schedules = dailyScheduleService.findByScheduleGroupId(scheduleGroupId);
            List<DailyScheduleDTO> holidaysOrRescheduled = schedules.stream()
                .filter(ds -> ds.isHoliday() || ds.isRescheduled())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return new ResponseEntity<>(holidaysOrRescheduled, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rescheduled/by-user/{userId}")
    public ResponseEntity<?> getRescheduledByUser(@PathVariable String userId) {
        try {
            List<DailySchedule> rescheduled = dailyScheduleService.findRescheduledByUserId(userId);
            List<DailyScheduleDTO> dtos = rescheduled.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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