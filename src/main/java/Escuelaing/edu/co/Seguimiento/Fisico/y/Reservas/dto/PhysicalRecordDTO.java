package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalRecordDTO {
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String physicalGoal;
    private String observations;
    private String activeRoutine;
}