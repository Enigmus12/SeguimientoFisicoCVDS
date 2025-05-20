// 1. En DailyScheduleDTO.java, añadimos la lista de usuarios
package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DailyScheduleDTO {
    private String id;
    private String scheduleId;
    private String scheduleGroupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private String dayOfWeek;
    private Integer capacity;
    private boolean isHoliday;
    private String holidayDescription;
    private boolean rescheduled;
    private List<String> users = new ArrayList<>();
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate originalDate;

    public DailyScheduleDTO(String id, String scheduleId, String scheduleGroupId, LocalDate date,
                            LocalTime startTime, LocalTime endTime, String dayOfWeek, Integer capacity,
                            boolean isHoliday, String holidayDescription, boolean rescheduled,
                            LocalDate originalDate) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.scheduleGroupId = scheduleGroupId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.capacity = capacity;
        this.isHoliday = isHoliday;
        this.holidayDescription = holidayDescription;
        this.rescheduled = rescheduled;
        this.originalDate = originalDate;
        this.users = new ArrayList<>();
        this.status = capacity > 0 ? "INCOMPLETE" : "FULL";
    }

    public DailyScheduleDTO(String id, String scheduleId, String scheduleGroupId, LocalDate date,
                            LocalTime startTime, LocalTime endTime, String dayOfWeek, Integer capacity,
                            boolean isHoliday, String holidayDescription, boolean rescheduled,
                            LocalDate originalDate, List<String> users, String status) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.scheduleGroupId = scheduleGroupId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.capacity = capacity;
        this.isHoliday = isHoliday;
        this.holidayDescription = holidayDescription;
        this.rescheduled = rescheduled;
        this.originalDate = originalDate;
        this.users = users != null ? users : new ArrayList<>();
        this.status = status;
    }
}