package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;

import java.util.List;

public interface PhysicalTrackingService {

    PhysicalRecord savePhysicalRecord(PhysicalRecordDTO recordDTO);

    PhysicalRecord createPhysicalRecord(PhysicalRecord record);

    List<PhysicalRecord> getAllRecords();

    PhysicalRecord getUserPhysicalHistory(String userName);

    PhysicalRecord getPhysicalRecord(String id);

    PhysicalRecord updatePhysicalRecord(PhysicalRecord record);
}