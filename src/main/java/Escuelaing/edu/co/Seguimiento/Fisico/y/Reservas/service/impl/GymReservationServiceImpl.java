package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.GymReservationMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.GymReservationService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.GymScheduleMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GymReservationServiceImpl implements GymReservationService {

    @Autowired
    private GymReservationMongoRepository gymReservationRepository;

    @Autowired
    private GymScheduleMongoRepository gymSchedulesRepository;

    @Override
    public List<GymReservation> createGroupReservation(String userId, String scheduleGroupId,
                                                       String userName, String identification,
                                                       String institutionRole, List<GymSchedules> schedules) {
        List<GymReservation> reservationsToSave = new ArrayList<>();
        List<GymReservation> savedReservations = new ArrayList<>();

        // Primero, crear todas las instancias de reserva sin guardarlas todavía
        for (GymSchedules schedule : schedules) {
            GymReservation reservation = new GymReservation();
            reservation.setUserId(userId);
            reservation.setScheduleId(schedule.getId());
            reservation.setScheduleGroupId(scheduleGroupId);
            reservation.setUserName(userName);
            reservation.setIdentification(identification);
            reservation.setInstitutionRole(institutionRole);
            reservation.setStartTime(schedule.getStartTime());
            reservation.setEndTime(schedule.getEndTime());
            reservation.setDayOfWeek(schedule.getDayOfWeek());

            // Si GymReservation tiene atributos de fecha, asignarlos aquí
            // Por ejemplo:
            // reservation.setDate(schedule.getStartDate());

            reservationsToSave.add(reservation);
        }

        // Luego, guardar todas las reservas y actualizar las capacidades
        for (GymReservation reservation : reservationsToSave) {
            // Guardar la reserva
            GymReservation savedReservation = gymReservationRepository.save(reservation);
            savedReservations.add(savedReservation);

            // Actualizar la capacidad del horario (reducir en 1)
            GymSchedules schedule = getGymScheduleById(reservation.getScheduleId());
            if (schedule != null && schedule.getCapacity() > 0) {
                schedule.setCapacity(schedule.getCapacity() - 1);
                gymSchedulesRepository.save(schedule);
            }
        }

        return savedReservations;
    }

    @Override
    public GymSchedules getGymScheduleById(String scheduleId) {
        Optional<GymSchedules> schedule = gymSchedulesRepository.findById(scheduleId);
        return schedule.orElse(null);
    }

    @Override
    public List<GymSchedules> getGymSchedulesByGroupId(String scheduleGroupId) {
        return gymSchedulesRepository.findByScheduleGroupId(scheduleGroupId);
    }

    @Override
    public boolean hasAvailableCapacity(String scheduleId) {
        GymSchedules schedule = getGymScheduleById(scheduleId);
        return schedule != null && schedule.getCapacity() > 0;
    }

    @Override
    public boolean hasUserReservation(String userId, String scheduleId) {
        // Verificar si el usuario ya tiene una reserva para este horario específico
        GymSchedules schedule = getGymScheduleById(scheduleId);
        if (schedule == null) {
            return false;
        }

        List<GymReservation> userReservations = gymReservationRepository.findByUserId(userId);
        for (GymReservation reservation : userReservations) {
            // Verificar si coincide el día y las horas
            if (reservation.getDayOfWeek().equals(schedule.getDayOfWeek()) &&
                    reservation.getStartTime().equals(schedule.getStartTime()) &&
                    reservation.getEndTime().equals(schedule.getEndTime())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<GymReservation> getUserReservations(String userId) {
        return gymReservationRepository.findByUserId(userId);
    }
}