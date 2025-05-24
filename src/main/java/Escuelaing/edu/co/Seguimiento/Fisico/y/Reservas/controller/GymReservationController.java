package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.GymReservationService;
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
@CrossOrigin(origins = "*")
public class GymReservationController {

    @Autowired
    private GymReservationService gymReservationService;

    @Autowired
    private JwtUtil jwtUtil;

    // Método actualizado para reservar con scheduleGroupId
    @PostMapping("/reserve-group/{scheduleGroupId}")
    public ResponseEntity<?> reserveGymGroup(
            @PathVariable String scheduleGroupId,
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

            // Obtener los horarios del grupo
            List<GymSchedules> schedules = gymReservationService.getGymSchedulesByGroupId(scheduleGroupId);
            if (schedules == null || schedules.isEmpty()) {
                return new ResponseEntity<>("Grupo de horarios no encontrado",
                        HttpStatus.NOT_FOUND);
            }

            // Verificar si hay capacidad disponible para todos los horarios del grupo
            for (GymSchedules schedule : schedules) {
                if (!gymReservationService.hasAvailableCapacity(schedule.getId())) {
                    return new ResponseEntity<>("No hay cupos disponibles para el horario " +
                            schedule.getDayOfWeek() + " de " + schedule.getStartTime() +
                            " a " + schedule.getEndTime(),
                            HttpStatus.BAD_REQUEST);
                }
            }

            // Verificar si el usuario ya tiene reservas para alguno de estos horarios
            for (GymSchedules schedule : schedules) {
                if (gymReservationService.hasUserReservation(userId, schedule.getId())) {
                    return new ResponseEntity<>("Ya tienes una reserva para el horario " +
                            schedule.getDayOfWeek() + " de " + schedule.getStartTime() +
                            " a " + schedule.getEndTime(),
                            HttpStatus.CONFLICT);
                }
            }

            // Crear las reservas para todos los horarios del grupo
            List<GymReservation> savedReservations = gymReservationService.createGroupReservation(
                    userId,
                    scheduleGroupId,
                    userData.getUserName(),
                    userData.getNumberId().toString(),
                    userData.getRole(),
                    schedules
            );

            // Invocar el método POST de DailyScheduleController para agregar al usuario a los horarios diarios
            String putUrl = "http://localhost:8080/daily-schedule/" + scheduleGroupId + "/add-user/" + userId;
            ResponseEntity<String> putResponse = restTemplate.exchange(
                    putUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (!putResponse.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity<>("Reserva creada pero ocurrió un error al agregar al usuario en los horarios diarios",
                        putResponse.getStatusCode());
            }

            return new ResponseEntity<>(savedReservations, HttpStatus.CREATED);

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
}