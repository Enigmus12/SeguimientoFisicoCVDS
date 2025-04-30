package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class GymSession {
    private String id;
    private Date startDateTime;
    private Date endDateTime;
    private int maxCapacity;
    private int availableSpots;
    private String responsibleTrainer;
    private List<GymReservation> reservations;
}
