package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhysicalTrackingServiceImpl implements PhysicalTrackingService {

    @Autowired
    private PhysicalRecordMongoRepository repository;

    @Override
    public PhysicalRecord createPhysicalRecord(PhysicalRecord record) {
        return repository.save(record);
    }

    @Override
    public PhysicalRecord getPhysicalRecord(String id) {
        Optional<PhysicalRecord> recordOptional = repository.findById(id);
        return recordOptional.orElse(null);
    }

    @Override
    public List<PhysicalRecord> getUserPhysicalHistory(String userId) {
        return repository.findByUserName(userId);
    }

    @Override
    public void updatePhysicalRecord(PhysicalRecord record) {
        repository.save(record);
    }

    @Override
    public List<PhysicalRecord> getAllrecords() {
        return repository.findAll();
    }
}