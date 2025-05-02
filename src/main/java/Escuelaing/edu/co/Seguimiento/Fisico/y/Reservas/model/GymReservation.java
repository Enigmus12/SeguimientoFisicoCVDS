package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class GymReservation {
    private String id;
    private String userName;
    private String identificationNumber;
    private String institutionRole;
    private Date reservationDateTime;
    private int durationMinutes;
    private String reservationStatus;  // Confirmed, Canceled, Completed
    private String assignedTrainer;
}