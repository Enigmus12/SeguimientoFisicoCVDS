package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class PhysicalRecord {
    private String id;
    private String userName;
    private String identificationNumber;
    private String institutionRole;
    private Date registrationDate;
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String physicalGoal;
    private String trainerNotes;
    private Routine activeRoutine;
}
