package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;

import java.util.List;

public interface GymReservationService {
    GymReservation createReservation(GymReservation reservation, String scheduleId);

    List<GymReservation> createGroupReservation(String userId, String scheduleGroupId,
                                                String userName, String identification,
                                                String institutionRole, List<GymSchedules> schedules);

    GymSchedules getGymScheduleById(String scheduleId);

    List<GymSchedules> getGymSchedulesByGroupId(String scheduleGroupId);

    boolean hasAvailableCapacity(String scheduleId);

    boolean hasUserReservation(String userId, String scheduleId);

    List<GymReservation> getUserReservations(String userId);

    boolean isUserReservation(String userId, String reservationId);

    boolean canCancelReservation(String reservationId);

    boolean cancelReservation(String reservationId);

    boolean cancelReservationWithReason(String reservationId, String cancellationReason);

    List<GymSchedules> getAllGymSchedules();

    List<GymSchedules> getAvailableGymSchedules();
}