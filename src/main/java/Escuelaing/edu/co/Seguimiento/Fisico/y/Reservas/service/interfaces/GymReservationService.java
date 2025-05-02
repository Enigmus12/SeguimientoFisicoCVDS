package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;

import java.util.Date;
import java.util.List;

public interface GymReservationService {
    GymReservation createReservation(GymReservation reservation);
    GymReservation getReservation(String id);
    List<GymReservation> getUserReservations(String userId);
    List<GymReservation> getReservationsByDate(Date date);
    void cancelReservation(String id);
}

