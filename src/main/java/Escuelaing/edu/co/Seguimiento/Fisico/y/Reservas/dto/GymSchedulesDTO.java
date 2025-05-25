package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymSchedulesDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-CO", timezone = "America/Bogota")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-CO", timezone = "America/Bogota")
    private LocalDate endDate;

    // Mapa donde la clave es el día de la semana y el valor es un array con dos elementos:
    // índice 0: hora de inicio, índice 1: hora de fin
    private Map<String, LocalTime[]> dayTimeMap;

    private Integer capacity;
}