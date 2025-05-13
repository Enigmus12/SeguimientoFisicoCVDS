package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;


import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "GymSchedules")
public class GymSchedules {
    @MongoId
    private String id;
    private String startTime;  // formato "HH:MM"
    private String endTime;    // formato "HH:MM"
    private String dayOfWeek;  // día de la semana (lunes, martes, etc.)
    private Integer capacity;

    @PersistenceConstructor
    public GymSchedules(String startTime, String endTime, String dayOfWeek, Integer capacity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.capacity = capacity;
    }

    public GymSchedules(GymSchedulesDTO gymSchedules) {
        this.startTime = gymSchedules.getStartTime();
        this.endTime = gymSchedules.getEndTime();
        this.dayOfWeek = gymSchedules.getDayOfWeek();
    }
}
