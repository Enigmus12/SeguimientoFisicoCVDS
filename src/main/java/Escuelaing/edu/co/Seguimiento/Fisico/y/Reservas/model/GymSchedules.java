package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Document(collection = "GymSchedules")
public class GymSchedules {
    @MongoId
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;    // Fecha de inicio

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;      // Fecha de fin

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;    // Hora de inicio

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;      // Hora de fin

    private String dayOfWeek;       // Día de la semana (MONDAY, TUESDAY, etc.)
    private Integer capacity;       // Capacidad del horario

    // Grupo de horarios (para vincular horarios del mismo semestre)
    private String scheduleGroupId;

    @PersistenceConstructor
    public GymSchedules(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
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