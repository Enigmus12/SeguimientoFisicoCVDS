package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.PhysicalTrackingService;
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
    public List<PhysicalRecord> getUserPhysicalHistory(String userName) {
        // Este método ahora debe ser explícito sobre que busca por userName
        return repository.findByUserName(userName);
    }

    @Override
    public List<PhysicalRecord> getUserPhysicalHistoryByUserId(String userId) {
        // Nuevo método para buscar por userId
        return repository.findByUserId(userId);
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