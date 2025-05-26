package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.PhysicalRecord;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Routine;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.PhysicalRecordMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.RoutineMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.RoutineService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//comentario

@Service
public class RoutineServiceImpl implements RoutineService {

  @Autowired
  private UserRepository userRepository;

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
    System.out.println("Routine ID: " + id + " aqui estamos" + optionalRoutine);
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

  @Override
  public void createRoutineForUser(String username, Routine routine) {
    User user = userRepository.findByUserName(username);
    if (user != null) {
      user.addRoutine(routine.getId(), routine);
      userRepository.save(user);
    } else {
      throw new RuntimeException("User not found: " + username);
    }
  }

  @Override
  public List<Routine> getUserRoutines(String username) {
    User user = userRepository.findByUserName(username);
    if (user != null) {
      return user.getRoutines().values().stream().toList();
    } else {
      throw new RuntimeException("User not found: " + username);
    }
  }

  @Override
  public Routine getUserRoutineById(String username, String routineId) {
    User user = userRepository.findByUserName(username);
    if (user != null) {
      Routine routine = user.getRoutines().get(routineId);
      if (routine != null) {
        return routine;
      } else {
        throw new RuntimeException("Routine not found: " + routineId);
      }
    } else {
      throw new RuntimeException("User not found: " + username);
    }
  }

  @Override
  public String updateUserRoutine(
    String username,
    String routineId,
    RoutineDTO routineDTO
  ) {
    User user = userRepository.findByUserName(username);
    if (user != null) {
      Routine routineNew = new Routine(routineDTO);
      user.modifyRoutine(routineId, routineNew);
      userRepository.save(user); // <-- ¡Guarda el usuario actualizado!
      return "Routine updated successfully for user: " + username;
    } else {
      throw new RuntimeException("User not found: " + username);
    }
  }
}
