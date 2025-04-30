package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhysicalTrackingServiceImpl implements PhysicalTrackingService {
    @Override
    public PhysicalRecord createPhysicalRecord(PhysicalRecord record) {
        // Basic implementation
        return record;
    }

    @Override
    public PhysicalRecord getPhysicalRecord(String id) {
        // Basic implementation
        return new PhysicalRecord();
    }

    @Override
    public List<PhysicalRecord> getUserPhysicalHistory(String userId) {
        // Basic implementation
        return new ArrayList<>();
    }

    @Override
    public void updatePhysicalRecord(PhysicalRecord record) {
        // Basic implementation
    }
}
