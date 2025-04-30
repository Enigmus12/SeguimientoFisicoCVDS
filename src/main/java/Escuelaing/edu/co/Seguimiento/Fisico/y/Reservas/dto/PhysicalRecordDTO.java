package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class PhysicalRecordDTO {
    private String id;
    private String userName;
    private String identification;
    private Date registrationDate;
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String fitnessGoal;
}
