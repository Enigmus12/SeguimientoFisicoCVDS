package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSession;

import java.util.Date;
import java.util.List;

public interface GymSessionService {
    GymSession createSession(GymSession session);
    GymSession getSession(String id);
    List<GymSession> getSessionsByDate(Date date);
    void updateAvailableSpots(String sessionId, int newSpots);
    boolean checkAvailability(String sessionId);
}

