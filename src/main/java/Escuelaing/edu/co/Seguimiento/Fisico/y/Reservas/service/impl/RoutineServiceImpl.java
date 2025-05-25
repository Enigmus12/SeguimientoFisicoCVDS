package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.RoutineMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.RoutineService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//comentario

@Service
public class RoutineServiceImpl implements RoutineService {

  @Autowired
  private RoutineMongoRepository routineRepository;

  @Autowired
  private PhysicalRecordMongoRepository physicalRecordRepo;

  @Override
  public Routine createRoutine(RoutineDTO routineDTO) {
    Routine routine = new Routine(routineDTO);
    return routineRepository.save(routine);
  }

  @Override
  public List<Routine> getAllRoutines() {
    return routineRepository.findAll();
  }

  @Override
  public Routine getRoutineById(String id) {
    Optional<Routine> optionalRoutine = routineRepository.findById(id);
    return optionalRoutine.orElse(null);
  }

  @Override
  public Routine updateRoutine(String id, RoutineDTO routineDTO) {
    Optional<Routine> optionalRoutine = routineRepository.findById(id);
    if (optionalRoutine.isPresent()) {
      Routine existingRoutine = optionalRoutine.get();

      // Actualizar los campos de la rutina existente
      existingRoutine.setDuration(routineDTO.getDuration());
      existingRoutine.setFrequency(routineDTO.getFrequency());

      return routineRepository.save(existingRoutine);
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
    List<PhysicalRecord> records = physicalRecordRepo.findByUserName(username);
    if (
      records == null ||
      records.isEmpty() ||
      records.get(0).getPhysicalGoal() == null ||
      records.get(0).getPhysicalGoal().isBlank()
    ) {
      return List.of();
    }
    PhysicalRecord record = records.get(0);
    return routineRepository.findByObjective(record.getPhysicalGoal());
  }
}
