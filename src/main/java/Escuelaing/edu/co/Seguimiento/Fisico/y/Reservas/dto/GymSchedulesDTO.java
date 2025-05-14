package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymSchedulesDTO {
    // Fecha de inicio del semestre
    private String startDate;

    // Fecha de fin del semestre
    private String endDate;

    // Hora de inicio (igual para todos los días seleccionados)
    private String startTime;

    // Hora de fin (igual para todos los días seleccionados)
    private String endTime;

    // Lista de días de la semana seleccionados (máximo 3)
    private List<String> daysOfWeek;

    // Capacidad (opcional)
    private Integer capacity;
}