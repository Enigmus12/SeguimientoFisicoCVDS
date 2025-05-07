package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalRecordDTO {
    private String userName;
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String physicalGoal;
}