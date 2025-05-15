package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.PhysicalRecordDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalTrackingServiceImpl implements PhysicalTrackingService {

    @Autowired
    private PhysicalRecordMongoRepository physicalRecordRepository;

    @Override
    public PhysicalRecord savePhysicalRecord(PhysicalRecordDTO recordDTO) {
        PhysicalRecord record = new PhysicalRecord(recordDTO);
        return physicalRecordRepository.save(record);
    }

    @Override
    public PhysicalRecord createPhysicalRecord(PhysicalRecord record) {
        return physicalRecordRepository.save(record);
    }

    @Override
    public List<PhysicalRecord> getAllRecords() {
        return physicalRecordRepository.findAll();
    }

    @Override
    public PhysicalRecord getUserPhysicalHistory(String userName) {
        return physicalRecordRepository.findByUserName(userName);
    }

    @Override
    public PhysicalRecord getPhysicalRecord(String id) {
        return physicalRecordRepository.findById(id).orElse(null);
    }

    @Override
    public PhysicalRecord updatePhysicalRecord(PhysicalRecord record) {
        return physicalRecordRepository.save(record);
    }
}