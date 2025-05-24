package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.GymSchedulesDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.GymScheduleMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.GymScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<String, LocalTime[]> dayTimeMap = gymSchedulesDTO.getDayTimeMap();

        // Validamos que se seleccionen máximo 3 días
        if (dayTimeMap == null || dayTimeMap.isEmpty() || dayTimeMap.size() > 3) {
            throw new IllegalArgumentException("Se deben seleccionar entre 1 y 3 días de la semana");
        }

        // Generamos un ID de grupo para vincular todos los horarios creados
        String scheduleGroupId = UUID.randomUUID().toString();

        // Obtener fechas de semestre y capacidad existentes si hay horarios
        List<GymSchedules> existingSchedules = gymScheduleMongoRepository.findAll();

        LocalDate startDate = gymSchedulesDTO.getStartDate();
        LocalDate endDate = gymSchedulesDTO.getEndDate();
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
            if (startDate == null) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la fecha de inicio del semestre");
            }
            if (endDate == null) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la fecha de fin del semestre");
            }
            if (capacity == null) {
                throw new IllegalArgumentException("Para el primer horario, debe especificar la capacidad");
            }
        }

        List<GymSchedules> createdSchedules = new ArrayList<>();

        // Para cada día seleccionado creamos un horario con sus horarios específicos
        for (Map.Entry<String, LocalTime[]> entry : dayTimeMap.entrySet()) {
            String dayOfWeek = entry.getKey();
            LocalTime[] times = entry.getValue();

            if (times == null || times.length != 2 || times[0] == null || times[1] == null) {
                throw new IllegalArgumentException("Se debe proporcionar un array con hora de inicio y fin para el día " + dayOfWeek);
            }

            LocalTime startTime = times[0];
            LocalTime endTime = times[1];

            // Validamos el formato de horas para cada día (deben tener intervalo de 1 hora)
            validateTimeFormat(startTime, endTime);

            // Verificamos si hay solapamiento de horarios para este día específico
            boolean hasOverlap = checkForTimeOverlap(
                    dayOfWeek,
                    startTime,
                    endTime,
                    startDate,
                    endDate
            );

            if (hasOverlap) {
                throw new IllegalStateException(
                        "Ya existe un horario para el día " + dayOfWeek +
                                " de " + startTime +
                                " a " + endTime +
                                " que se solapa con el periodo seleccionado"
                );
            }

            // Creamos el horario para este día
            GymSchedules gymSchedule = new GymSchedules(
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    dayOfWeek,
                    capacity,
                    scheduleGroupId
            );

            createdSchedules.add(gymScheduleMongoRepository.save(gymSchedule));
        }

        return createdSchedules;
    }

    /**
     * Verifica si existe solapamiento de horarios para un día y rango de horas específico
     */
    private boolean checkForTimeOverlap(String dayOfWeek, LocalTime startTime, LocalTime endTime,
                                        LocalDate startDate, LocalDate endDate) {
        // Primero obtenemos horarios que coincidan con el día de la semana y el periodo del semestre
        List<GymSchedules> sameWeekdaySchedules = gymScheduleMongoRepository
                .findByDayOfWeekAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        dayOfWeek, endDate, startDate);

        // Luego verificamos solapamiento de horas
        return sameWeekdaySchedules.stream().anyMatch(schedule -> {
            // Hay solapamiento si:
            // 1. El horario existente comienza durante nuestro nuevo horario
            boolean existingStartsDuringNew =
                    (schedule.getStartTime().compareTo(startTime) >= 0 &&
                            schedule.getStartTime().compareTo(endTime) < 0);

            // 2. El horario existente termina durante nuestro nuevo horario
            boolean existingEndsDuringNew =
                    (schedule.getEndTime().compareTo(startTime) > 0 &&
                            schedule.getEndTime().compareTo(endTime) <= 0);

            // 3. El horario existente abarca completamente nuestro nuevo horario
            boolean existingContainsNew =
                    (schedule.getStartTime().compareTo(startTime) <= 0 &&
                            schedule.getEndTime().compareTo(endTime) >= 0);

            // 4. Nuestro nuevo horario abarca completamente el horario existente
            boolean newContainsExisting =
                    (startTime.compareTo(schedule.getStartTime()) <= 0 &&
                            endTime.compareTo(schedule.getEndTime()) >= 0);

            return existingStartsDuringNew || existingEndsDuringNew ||
                    existingContainsNew || newContainsExisting;
        });
    }

    /**
     * Método alternativo que usa la consulta MongoDB directa para verificar solapamientos
     */
    private boolean checkForOverlappingSchedules(String dayOfWeek, LocalTime startTime, LocalTime endTime,
                                                 LocalDate startDate, LocalDate endDate) {
        // Usamos la consulta MongoDB directamente
        List<GymSchedules> overlappingSchedules = gymScheduleMongoRepository.findOverlappingSchedules(
                dayOfWeek, startTime, endTime, startDate, endDate
        );

        return !overlappingSchedules.isEmpty();
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
    private void validateTimeFormat(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Las horas de inicio y fin son obligatorias");
        }

        // Convertimos a minutos para comparar
        int startTotalMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endTotalMinutes = endTime.getHour() * 60 + endTime.getMinute();

        // La diferencia debe ser exactamente de 60 minutos (1 hora)
        if (endTotalMinutes - startTotalMinutes != 60) {
            throw new IllegalArgumentException("Los horarios deben tener un intervalo exacto de 1 hora");
        }
    }
}