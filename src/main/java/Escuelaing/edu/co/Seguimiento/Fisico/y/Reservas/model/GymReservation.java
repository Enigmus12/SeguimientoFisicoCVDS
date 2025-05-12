package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
@NoArgsConstructor
@Document(collection = "GymReservations")
public class GymReservation {
    @MongoId
    private String id;
    private String userId;
    private String scheduleId;
    private String userName;
    private String identification;
    private String institutionRole;
    private String startTime;
    private String endTime;
    private String dayOfWeek;

}