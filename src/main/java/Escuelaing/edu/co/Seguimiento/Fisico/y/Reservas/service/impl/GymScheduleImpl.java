package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GymScheduleImpl implements GymScheduleService {

    private final GymScheduleMongoRepository gymScheduleMongoRepository;

    @Autowired
    public GymScheduleImpl(GymScheduleMongoRepository gymScheduleMongoRepository) {
        this.gymScheduleMongoRepository = gymScheduleMongoRepository;
    }

    @Override
    public List<GymSchedules> findAll() {
        return gymScheduleMongoRepository.findAll();
    }

    @Override
    public Optional<GymSchedules> findById(String id) {
        return gymScheduleMongoRepository.findById(id);
    }

    @Override
    public GymSchedules create(GymSchedulesDTO gymSchedulesDTO) {
        GymSchedules gymSchedules = new GymSchedules(gymSchedulesDTO);
        // Inicializamos la capacidad como null
        gymSchedules.setCapacity(null);
        return gymScheduleMongoRepository.save(gymSchedules);
    }

    @Override
    public GymSchedules updateCapacity(String id, Integer capacity) {
        Optional<GymSchedules> optionalGymSchedules = gymScheduleMongoRepository.findById(id);
        if (optionalGymSchedules.isPresent()) {
            GymSchedules gymSchedules = optionalGymSchedules.get();
            gymSchedules.setCapacity(capacity);
            return gymScheduleMongoRepository.save(gymSchedules);
        }
        return null;
    }
}