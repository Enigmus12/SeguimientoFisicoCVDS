package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;

import java.util.List;

public interface GymReservationService {

    GymReservation createReservation(GymReservation reservation, String scheduleId);
    GymSchedules getGymScheduleById(String scheduleId);
    boolean hasAvailableCapacity(String scheduleId);
    boolean hasUserReservation(String userId, String scheduleId);
    List<GymReservation> getUserReservations(String userId);
    boolean isUserReservation(String userId, String reservationId);
    boolean cancelReservation(String reservationId);
    List<GymSchedules> getAllGymSchedules();
    List<GymSchedules> getAvailableGymSchedules();
    boolean canCancelReservation(String reservationId);
}