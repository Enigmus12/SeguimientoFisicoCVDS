package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoutineServiceImpl implements RoutineService {

    @Autowired
    private RoutineMongoRepository routineRepository;

    @Autowired
    private PhysicalRecordMongoRepository physicalRecordRepo;

    @Override
    public Routine createRoutine(RoutineDTO routineDTO) {
        return routineRepository.save(new Routine(routineDTO));
    }

    @Override
    public Routine saveRoutine(Routine routine) {
        return routineRepository.save(routine);
    }

    @Override
    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }

    @Override
    public Routine getRoutineById(String id) {
        return routineRepository.findById(id).orElse(null);
    }

    @Override
    public Routine updateRoutine(String id, RoutineDTO routineDTO) {
        Optional<Routine> optional = routineRepository.findById(id);
        if (optional.isPresent()) {
            Routine r = optional.get();
            r.setDuration(routineDTO.getDuration());
            r.setFrequency(routineDTO.getFrequency());
            return routineRepository.save(r);
        }
        return null;
    }

    @Override
    public boolean deleteRoutine(String id) {
        if (routineRepository.existsById(id)) {
            routineRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Routine> getRecommendations(String username) {
        PhysicalRecord record = physicalRecordRepo.findByUserName(username);
        if (record == null || record.getPhysicalGoal() == null || record.getPhysicalGoal().isBlank()) {
            return List.of();
        }

        return routineRepository.findByObjective(record.getPhysicalGoal());
    }

}