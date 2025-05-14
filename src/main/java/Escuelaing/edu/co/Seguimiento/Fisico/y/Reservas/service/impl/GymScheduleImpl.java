package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<GymSchedules> createSemestralSchedules(GymSchedulesDTO gymSchedulesDTO) {
        // Validamos que se seleccionen máximo 3 días
        if (gymSchedulesDTO.getDaysOfWeek() == null || gymSchedulesDTO.getDaysOfWeek().isEmpty() ||
                gymSchedulesDTO.getDaysOfWeek().size() > 3) {
            throw new IllegalArgumentException("Se deben seleccionar entre 1 y 3 días de la semana");
        }

        // Validamos el formato de horas (deben tener intervalo de 1 hora)
        validateTimeFormat(gymSchedulesDTO.getStartTime(), gymSchedulesDTO.getEndTime());

        // Generamos un ID de grupo para vincular todos los horarios creados
        String scheduleGroupId = UUID.randomUUID().toString();

        // Obtener fechas de semestre y capacidad existentes si hay horarios
        List<GymSchedules> existingSchedules = gymScheduleMongoRepository.findAll();

        String startDate = gymSchedulesDTO.getStartDate();
        String endDate = gymSchedulesDTO.getEndDate();
        Integer capacity = gymSchedulesDTO.getCapacity();

        // Si hay horarios existentes, usamos su información de fechas y capacidad
        if (!existingSchedules.isEmpty()) {
            GymSchedules existingSchedule = existingSchedules.get(0);
            startDate = existingSchedule.getStartDate();
            endDate = existingSchedule.getEndDate();

            // Si no se especificó una capacidad, usar la de los horarios existentes
            if (capacity == null) {
                capacity = existingSchedule.getCapacity();
            }
        } else {
            // Si es el primer horario, validamos que se proporcionen todos los datos requeridos
            if (startDate == null || startDate.isEmpty()) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la fecha de inicio del semestre");
            }
            if (endDate == null || endDate.isEmpty()) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la fecha de fin del semestre");
            }
            if (capacity == null) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la capacidad");
            }
        }

        List<GymSchedules> createdSchedules = new ArrayList<>();

        // Para cada día seleccionado creamos un horario
        for (String dayOfWeek : gymSchedulesDTO.getDaysOfWeek()) {
            // Verificamos si hay solapamiento con horarios existentes
            List<GymSchedules> overlappingSchedules = gymScheduleMongoRepository.findOverlappingSchedules(
                    dayOfWeek,
                    gymSchedulesDTO.getStartTime(),
                    gymSchedulesDTO.getEndTime(),
                    startDate,
                    endDate
            );

            if (!overlappingSchedules.isEmpty()) {
                throw new IllegalStateException(
                        "Ya existe un horario para el día " + dayOfWeek +
                                " de " + gymSchedulesDTO.getStartTime() +
                                " a " + gymSchedulesDTO.getEndTime() +
                                " que se solapa con el periodo seleccionado"
                );
            }

            // Creamos el horario para este día
            GymSchedules gymSchedule = new GymSchedules(
                    startDate,
                    endDate,
                    gymSchedulesDTO.getStartTime(),
                    gymSchedulesDTO.getEndTime(),
                    dayOfWeek,
                    capacity,
                    scheduleGroupId
            );

            createdSchedules.add(gymScheduleMongoRepository.save(gymSchedule));
        }

        return createdSchedules;
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

    @Override
    public List<GymSchedules> updateGroupCapacity(String scheduleGroupId, Integer capacity) {
        List<GymSchedules> schedules = gymScheduleMongoRepository.findByScheduleGroupId(scheduleGroupId);
        List<GymSchedules> updatedSchedules = new ArrayList<>();

        for (GymSchedules schedule : schedules) {
            schedule.setCapacity(capacity);
            updatedSchedules.add(gymScheduleMongoRepository.save(schedule));
        }

        return updatedSchedules;
    }

    @Override
    public List<GymSchedules> updateAllSchedulesCapacity(Integer capacity) {
        List<GymSchedules> allSchedules = gymScheduleMongoRepository.findAll();
        List<GymSchedules> updatedSchedules = new ArrayList<>();

        for (GymSchedules schedule : allSchedules) {
            schedule.setCapacity(capacity);
            updatedSchedules.add(gymScheduleMongoRepository.save(schedule));
        }

        return updatedSchedules;
    }

    // Método auxiliar para validar el formato de las horas
    private void validateTimeFormat(String startTime, String endTime) {
        // Verificamos que las horas tengan el formato correcto (HH:MM)
        if (!startTime.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") ||
                !endTime.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IllegalArgumentException("El formato de hora debe ser HH:MM");
        }

        // Verificamos que la hora de fin sea mayor que la hora de inicio
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int endHour = Integer.parseInt(endParts[0]);

        int startMinute = Integer.parseInt(startParts[1]);
        int endMinute = Integer.parseInt(endParts[1]);

        // Convertimos a minutos para comparar
        int startTotalMinutes = startHour * 60 + startMinute;
        int endTotalMinutes = endHour * 60 + endMinute;

        // La diferencia debe ser exactamente de 60 minutos (1 hora)
        if (endTotalMinutes - startTotalMinutes != 60) {
            throw new IllegalArgumentException("Los horarios deben tener un intervalo exacto de 1 hora");
        }
    }
}