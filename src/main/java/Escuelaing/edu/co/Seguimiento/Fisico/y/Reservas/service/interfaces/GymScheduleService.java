package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;

import java.util.List;
import java.util.Optional;

public interface GymScheduleService {
    List<GymSchedules> findAll();
    Optional<GymSchedules> findById(String id);
    List<GymSchedules> createSemestralSchedules(GymSchedulesDTO gymSchedulesDTO);
    GymSchedules updateCapacity(String id, Integer capacity);
    List<GymSchedules> updateGroupCapacity(String scheduleGroupId, Integer capacity);
    List<GymSchedules> updateAllSchedulesCapacity(Integer capacity);
}