package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymReservationMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymReservationService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleMongoRepository;
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
    public GymReservation createReservation(GymReservation reservation, String scheduleId) {
        // Guardar la reserva
        GymReservation savedReservation = gymReservationRepository.save(reservation);

        // Actualizar la capacidad del horario (reducir en 1)
        GymSchedules schedule = getGymScheduleById(scheduleId);
        if (schedule != null && schedule.getCapacity() > 0) {
            schedule.setCapacity(schedule.getCapacity() - 1);
            gymSchedulesRepository.save(schedule);
        }

        return savedReservation;
    }

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

    @Override
    public boolean isUserReservation(String userId, String reservationId) {
        Optional<GymReservation> reservation = gymReservationRepository.findById(reservationId);
        return reservation.isPresent() && reservation.get().getUserId().equals(userId);
    }

    @Override
    public boolean canCancelReservation(String reservationId) {
        Optional<GymReservation> reservationOpt = gymReservationRepository.findById(reservationId);
        if (!reservationOpt.isPresent()) {
            return false;
        }

        GymReservation reservation = reservationOpt.get();
        LocalDateTime now = LocalDateTime.now();

        // Obtener día actual y día de la reserva
        String dayOfWeek = reservation.getDayOfWeek().toLowerCase();
        DayOfWeek today = now.getDayOfWeek();

        // Convertir día de la semana en español a DayOfWeek de Java
        DayOfWeek reservationDay;
        switch (dayOfWeek) {
            case "lunes": reservationDay = DayOfWeek.MONDAY; break;
            case "martes": reservationDay = DayOfWeek.TUESDAY; break;
            case "miércoles": case "miercoles": reservationDay = DayOfWeek.WEDNESDAY; break;
            case "jueves": reservationDay = DayOfWeek.THURSDAY; break;
            case "viernes": reservationDay = DayOfWeek.FRIDAY; break;
            case "sábado": case "sabado": reservationDay = DayOfWeek.SATURDAY; break;
            case "domingo": reservationDay = DayOfWeek.SUNDAY; break;
            default: reservationDay = DayOfWeek.MONDAY;
        }

        // Convertir la hora de inicio de string a LocalTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime reservationTime = LocalTime.parse(reservation.getStartTime(), formatter);

        // Calcular la próxima fecha de la reserva
        LocalDate nextReservationDate = now.toLocalDate().with(TemporalAdjusters.nextOrSame(reservationDay));

        // Si es el mismo día, verificar si ya pasó la hora
        if (today == reservationDay) {
            // Si ya pasó la hora del día actual, entonces la reserva sería para la próxima semana
            if (now.toLocalTime().isAfter(reservationTime)) {
                nextReservationDate = now.toLocalDate().with(TemporalAdjusters.next(reservationDay));
            }
        } else if (today.getValue() > reservationDay.getValue()) {
            // Si el día actual está después del día de la reserva, la reserva es para la próxima semana
            nextReservationDate = now.toLocalDate().with(TemporalAdjusters.next(reservationDay));
        }

        // Combinar fecha y hora para obtener la fecha y hora exacta de la reserva
        LocalDateTime reservationDateTime = LocalDateTime.of(nextReservationDate, reservationTime);

        // Verificar si faltan al menos 5 horas para la reserva
        return now.plusHours(5).isBefore(reservationDateTime);
    }

    @Override
    public boolean cancelReservation(String reservationId) {
        try {
            // Verificar si se puede cancelar la reserva (con 5 horas de anticipación)
            if (!canCancelReservation(reservationId)) {
                return false;
            }

            Optional<GymReservation> reservationOpt = gymReservationRepository.findById(reservationId);
            if (!reservationOpt.isPresent()) {
                return false;
            }

            GymReservation reservation = reservationOpt.get();

            // Incrementar la capacidad del horario correspondiente
            increaseScheduleCapacity(reservation);

            // Eliminar la reserva
            gymReservationRepository.deleteById(reservationId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cancelReservationWithReason(String reservationId, String cancellationReason) {
        try {
            // Verificar si se puede cancelar la reserva (con 5 horas de anticipación)
            if (!canCancelReservation(reservationId)) {
                return false;
            }

            Optional<GymReservation> reservationOpt = gymReservationRepository.findById(reservationId);
            if (!reservationOpt.isPresent()) {
                return false;
            }

            GymReservation reservation = reservationOpt.get();

            // Guardar la razón de cancelación (aunque vamos a eliminar la reserva, esto podría
            // guardarse en una colección separada para estadísticas si se desea en el futuro)
            reservation.setCancellationReason(cancellationReason);

            // Incrementar la capacidad del horario correspondiente
            increaseScheduleCapacity(reservation);

            // Eliminar la reserva
            gymReservationRepository.deleteById(reservationId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método auxiliar para aumentar la capacidad del horario
    private void increaseScheduleCapacity(GymReservation reservation) {
        // Buscar el horario correspondiente para incrementar su capacidad
        List<GymSchedules> schedules = gymSchedulesRepository.findAll();
        for (GymSchedules schedule : schedules) {
            if (schedule.getDayOfWeek().equals(reservation.getDayOfWeek()) &&
                    schedule.getStartTime().equals(reservation.getStartTime()) &&
                    schedule.getEndTime().equals(reservation.getEndTime())) {
                // Incrementar la capacidad del horario
                schedule.setCapacity(schedule.getCapacity() + 1);
                gymSchedulesRepository.save(schedule);
                break;
            }
        }
    }

    @Override
    public List<GymSchedules> getAllGymSchedules() {
        return gymSchedulesRepository.findAll();
    }

    @Override
    public List<GymSchedules> getAvailableGymSchedules() {
        List<GymSchedules> allSchedules = gymSchedulesRepository.findAll();
        return allSchedules.stream()
                .filter(schedule -> schedule.getCapacity() > 0)
                .collect(Collectors.toList());
    }
}