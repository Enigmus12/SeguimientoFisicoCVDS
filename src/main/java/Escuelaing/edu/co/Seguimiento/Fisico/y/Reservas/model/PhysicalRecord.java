package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import java.util.Map;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "PhysicalRecords")
public class PhysicalRecord {
    @Id
    private String id; // Identificador único del registro
    private String userName;
    private Date registrationDate;
    private double weight;
    private Map<String, Double> bodyMeasurements;
    private String physicalGoal;

    @PersistenceCreator
    public PhysicalRecord(String id, String userName, Date registrationDate, double weight, Map<String, Double> bodyMeasurements, String physicalGoal) {
        this.id = id;
        this.userName = userName;
        this.registrationDate = registrationDate;
        this.weight = weight;
        this.bodyMeasurements = bodyMeasurements;
        this.physicalGoal = physicalGoal;
    }

    public PhysicalRecord(PhysicalRecordDTO physicalRecordDTO) {
        this.userName = physicalRecordDTO.getUserName();
        this.registrationDate = new Date(); // Asigna la fecha actual
        this.weight = physicalRecordDTO.getWeight();
        this.bodyMeasurements = physicalRecordDTO.getBodyMeasurements();
        this.physicalGoal = physicalRecordDTO.getPhysicalGoal();
    }
}