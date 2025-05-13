package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymReservationService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/gym")
public class GymReservationController {

    @Autowired
    private GymReservationService gymReservationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/reserve/{scheduleId}")
    public ResponseEntity<?> reserveGym(
            @PathVariable String scheduleId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Extraer el token del header
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            // Llamar al endpoint para obtener los datos del usuario
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<UserResponseDTO> userResponse = restTemplate.exchange(
                    "http://localhost:8080/user-service/users/" + userId,
                    HttpMethod.GET,
                    entity,
                    UserResponseDTO.class
            );

            if (userResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Error al obtener datos del usuario",
                        userResponse.getStatusCode());
            }

            UserResponseDTO userData = userResponse.getBody();

            // Obtener el horario del gimnasio
            GymSchedules schedule = gymReservationService.getGymScheduleById(scheduleId);
            if (schedule == null) {
                return new ResponseEntity<>("Horario de gimnasio no encontrado",
                        HttpStatus.NOT_FOUND);
            }

            // Verificar si hay capacidad disponible
            if (!gymReservationService.hasAvailableCapacity(scheduleId)) {
                return new ResponseEntity<>("No hay cupos disponibles para este horario",
                        HttpStatus.BAD_REQUEST);
            }

            // Verificar si el usuario ya tiene una reserva para este horario
            if (gymReservationService.hasUserReservation(userId, scheduleId)) {
                return new ResponseEntity<>("Ya tienes una reserva para este horario",
                        HttpStatus.CONFLICT);
            }

            // Crear la reserva
            GymReservation reservation = new GymReservation();
            reservation.setUserId(userId);
            reservation.setScheduleId(scheduleId);
            reservation.setUserName(userData.getUserName());
            reservation.setIdentification(userData.getNumberId().toString());
            reservation.setInstitutionRole(userData.getRole());
            reservation.setStartTime(schedule.getStartTime());
            reservation.setEndTime(schedule.getEndTime());
            reservation.setDayOfWeek(schedule.getDayOfWeek());

            // Guardar la reserva
            GymReservation savedReservation = gymReservationService.createReservation(reservation, scheduleId);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);

        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Usuario no encontrado: " + e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<?> getMyReservations(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extraer el token del header
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            // Obtener las reservas del usuario
            List<GymReservation> userReservations = gymReservationService.getUserReservations(userId);
            return new ResponseEntity<>(userReservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener las reservas: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cancel-reservation/{reservationId}")
    public ResponseEntity<?> cancelReservation(
            @PathVariable String reservationId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extraer el token del header
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            // Verificar si la reserva pertenece al usuario
            boolean isUserReservation = gymReservationService.isUserReservation(userId, reservationId);
            if (!isUserReservation) {
                return new ResponseEntity<>("No tienes permiso para cancelar esta reserva",
                        HttpStatus.FORBIDDEN);
            }

            // Verificar si la reserva se puede cancelar (5 horas antes)
            if (!gymReservationService.canCancelReservation(reservationId)) {
                return new ResponseEntity<>("Las reservas solo pueden cancelarse con al menos 5 horas de anticipación",
                        HttpStatus.BAD_REQUEST);
            }

            // Cancelar la reserva
            boolean canceled = gymReservationService.cancelReservation(reservationId);
            if (canceled) {
                return new ResponseEntity<>("Reserva cancelada exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se pudo cancelar la reserva",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al cancelar la reserva: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<?> getAllSchedules() {
        try {
            List<GymSchedules> schedules = gymReservationService.getAllGymSchedules();
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los horarios: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/schedules/available")
    public ResponseEntity<?> getAvailableSchedules() {
        try {
            List<GymSchedules> availableSchedules = gymReservationService.getAvailableGymSchedules();
            return new ResponseEntity<>(availableSchedules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los horarios disponibles: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}