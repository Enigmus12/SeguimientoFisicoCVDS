package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import java.util.Map;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document(collection = "PhysicalRecords")
public class PhysicalRecord {
    @MongoId
    private String id; // Identificador único del registro
    private String userId;
    private String userName;
    private Integer numberId;
    private String role;
    private Date registrationDate;
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String physicalGoal;
    private String observations;
    private String activeRoutine;

    @PersistenceCreator
    public PhysicalRecord(String id, String userId ,String userName,Integer numberId ,String role ,Date registrationDate, double weight, Map<String, Double> bodyMeasurements, String physicalGoal, String observations, String activeRoutine) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.numberId = numberId;
        this.role = role;
        this.registrationDate = registrationDate;
        this.weight = weight;
        this.bodyMeasurements = bodyMeasurements;
        this.physicalGoal = physicalGoal;
        this.observations = observations;
        this.activeRoutine = activeRoutine;
    }

    public PhysicalRecord(PhysicalRecordDTO physicalRecordDTO) {
        this.registrationDate = new Date(); // Asigna la fecha actual
        this.weight = physicalRecordDTO.getWeight();
        this.bodyMeasurements = physicalRecordDTO.getBodyMeasurements();
        this.physicalGoal = physicalRecordDTO.getPhysicalGoal();
        this.observations = (physicalRecordDTO.getObservations() != null) ?
                physicalRecordDTO.getObservations() : "";
        this.activeRoutine = (physicalRecordDTO.getActiveRoutine() != null) ?
                physicalRecordDTO.getActiveRoutine() : "";
    }
}