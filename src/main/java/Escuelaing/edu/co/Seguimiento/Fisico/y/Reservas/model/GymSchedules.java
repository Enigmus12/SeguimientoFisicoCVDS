package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document(collection = "GymSchedules")
public class GymSchedules {
    @MongoId
    private String id;
    private String startDate;    // Fecha de inicio (formato "yyyy-MM-dd")
    private String endDate;      // Fecha de fin (formato "yyyy-MM-dd")
    private String startTime;    // Hora de inicio (formato "HH:MM")
    private String endTime;      // Hora de fin (formato "HH:MM")
    private String dayOfWeek;    // Día de la semana (MONDAY, TUESDAY, etc.)
    private Integer capacity;    // Capacidad del horario

    // Grupo de horarios (para vincular horarios del mismo semestre)
    private String scheduleGroupId;

    @PersistenceConstructor
    public GymSchedules(String startDate, String endDate, String startTime, String endTime,
                        String dayOfWeek, Integer capacity, String scheduleGroupId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.capacity = capacity;
        this.scheduleGroupId = scheduleGroupId;
    }
}