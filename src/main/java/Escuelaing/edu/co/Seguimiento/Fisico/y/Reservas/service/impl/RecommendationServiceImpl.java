package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RecommendationService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private PhysicalRecordMongoRepository physicalRecordRepository;

    @Autowired
    private RoutineMongoRepository routineRepository;

    @Override
    public List<Routine> recommendRoutineForUser(String userId) {
        PhysicalRecord record = physicalRecordRepository.findByUserName(userId);
        if (record == null) {
            throw new RuntimeException("No se encontró historial físico para el usuario: " + userId);
        }
        String goal = record.getPhysicalGoal();
        return routineRepository.findByObjective(goal);
    }
}