package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
@Document(collection = "users")
public class User {
    private String id;
    private String name;
    private String identificationNumber;
    private String institutionRole;
    private String institutionalEmail;
    private List<PhysicalRecord> physicalHistory;
    private List<GymReservation> gymReservations;
}
