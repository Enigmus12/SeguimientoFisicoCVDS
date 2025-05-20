package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "DailySchedule")
public class DailySchedule {
    @MongoId
    private String id;
    private String scheduleId;          // ID del horario semestral original
    private String scheduleGroupId;     // ID del grupo de horarios

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;             // Fecha específica del horario diario

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;          // Hora de fin

    private String dayOfWeek;           // Día de la semana
    private Integer capacity;           // Capacidad
    private boolean isHoliday;          // Indica si el día es festivo
    private String holidayDescription;  // Descripción del festivo (si aplica)
    private boolean rescheduled;        // Indica si fue reprogramado por ser festivo
    private List<String> users = new ArrayList<>();  // Lista de usuarios (inicializada vacía)
    private String status;              // Estado: "FULL" o "INCOMPLETE"

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate originalDate;     // Fecha original antes de reprogramar (si aplica)

    public DailySchedule(String scheduleId, String scheduleGroupId, LocalDate date,
                         LocalTime startTime, LocalTime endTime, String dayOfWeek,
                         Integer capacity) {
        this.scheduleId = scheduleId;
        this.scheduleGroupId = scheduleGroupId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.capacity = capacity;
        this.isHoliday = false;
        this.rescheduled = false;
        this.users = new ArrayList<>();
        this.status = capacity > 0 ? "INCOMPLETE" : "FULL";
    }

    /**
     * Actualiza el estado basado en la capacidad actual
     */
    public void updateStatus() {
        this.status = this.capacity > 0 ? "INCOMPLETE" : "FULL";
    }
}