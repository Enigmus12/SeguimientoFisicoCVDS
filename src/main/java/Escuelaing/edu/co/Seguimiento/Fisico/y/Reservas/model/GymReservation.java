package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", locale = "es-CO", timezone = "America/Bogota")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", locale = "es-CO", timezone = "America/Bogota")
    private LocalTime endTime;

    private String dayOfWeek;
}