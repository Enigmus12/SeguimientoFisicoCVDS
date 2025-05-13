package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymSchedulesDTO {
    private String startTime;
    private String endTime;
    private String dayOfWeek;

}
