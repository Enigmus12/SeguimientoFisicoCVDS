package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;

import java.util.List;

public interface PhysicalTrackingService {
    PhysicalRecord createPhysicalRecord(PhysicalRecord record);
    PhysicalRecord getPhysicalRecord(String id);
    List<PhysicalRecord> getUserPhysicalHistory(String userId);
    void updatePhysicalRecord(PhysicalRecord record);
    List<PhysicalRecord> getAllrecords();
}
