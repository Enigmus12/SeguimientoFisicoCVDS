package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import java.util.Date;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class GymReservationDTO {
    private String id;
    private String userName;
    private String identification;
    private Date dateTime;
    private int duration;
    private String status;
}