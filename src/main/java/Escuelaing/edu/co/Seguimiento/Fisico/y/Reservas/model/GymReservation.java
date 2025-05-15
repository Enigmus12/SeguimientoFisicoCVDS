package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document(collection = "GymReservations")
public class GymReservation {
    @MongoId
    private String id;
    private String userId;
    private String scheduleId;
    private String scheduleGroupId; // campo para el grupo de horarios
    private String userName;
    private String identification;
    private String institutionRole;
    private String startTime;
    private String endTime;
    private String dayOfWeek;
    private String cancellationReason; // campo para la razón de cancelación
}