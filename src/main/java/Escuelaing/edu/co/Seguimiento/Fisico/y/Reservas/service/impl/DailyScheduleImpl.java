package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.DailySchedule;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSchedules;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Holiday;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.DailyScheduleMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.GymScheduleMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.DailyScheduleService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.GymScheduleService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.time.temporal.TemporalAdjusters;


@Service
public class DailyScheduleImpl implements DailyScheduleService {

    @Override
    public List<DailySchedule> generateDailySchedulesFromGroup(String scheduleGroupId) {
        // Obtener todos los horarios semestrales asociados al grupo
        List<GymSchedules> semestralSchedules = gymScheduleMongoRepository.findByScheduleGroupId(scheduleGroupId);
        if (semestralSchedules == null || semestralSchedules.isEmpty()) {
            throw new IllegalArgumentException("No existen horarios semestrales para el grupo con ID " + scheduleGroupId);
        }
        List<DailySchedule> allGenerated = new ArrayList<>();
        for (GymSchedules semestralSchedule : semestralSchedules) {
            // Reutilizar la lógica existente para cada horario semestral
            allGenerated.addAll(generateDailySchedulesFromSemestral(semestralSchedule.getId()));
        }
        return allGenerated;
    }

    @Autowired
    private DailyScheduleMongoRepository dailyScheduleRepository;

    @Autowired
    private GymScheduleService gymScheduleService;

    @Autowired
    private GymScheduleMongoRepository gymScheduleMongoRepository;

    @Autowired
    private HolidayService holidayService;

    @Override
    public List<DailySchedule> generateDailySchedulesFromSemestral(String scheduleId) {
        // Obtener el horario semestral
        Optional<GymSchedules> optionalSchedule = gymScheduleService.findById(scheduleId);
        if (!optionalSchedule.isPresent()) {
            throw new IllegalArgumentException("El horario semestral con ID " + scheduleId + " no existe");
        }

        GymSchedules semestralSchedule = optionalSchedule.get();

        // Ya no necesitamos parsear strings, usamos directamente LocalDate
        LocalDate startDate = semestralSchedule.getStartDate();
        LocalDate endDate = semestralSchedule.getEndDate();

        // Convertir el día de la semana de String a DayOfWeek
        DayOfWeek targetDayOfWeek = DayOfWeek.valueOf(semestralSchedule.getDayOfWeek());

        // Lista para almacenar los horarios diarios generados
        List<DailySchedule> generatedSchedules = new ArrayList<>();

        // Encontrar el primer día del periodo que coincida con el día de la semana objetivo
        LocalDate firstDateOfSchedule = startDate;

        // Si la fecha de inicio no coincide con el día de la semana objetivo,
        // ajustar a la primera fecha que sí coincida
        if (firstDateOfSchedule.getDayOfWeek() != targetDayOfWeek) {
            firstDateOfSchedule = startDate.with(TemporalAdjusters.nextOrSame(targetDayOfWeek));
        }

        // Obtener los días de la semana ocupados por este grupo de horarios
        Set<DayOfWeek> occupiedDays = getOccupiedDaysOfWeekByGroupId(semestralSchedule.getScheduleGroupId());

        // Iterar desde la primera fecha válida hasta la fecha de fin, saltando de semana en semana
        LocalDate currentDate = firstDateOfSchedule;
        while (!currentDate.isAfter(endDate)) {
            // Verificar si es un día festivo
            boolean isHoliday = holidayService.isHoliday(currentDate);
            Optional<Holiday> optionalHoliday = holidayService.getHolidayByDate(currentDate);

            if (isHoliday) {
                // Si es un día festivo, tratar de reprogramar
                LocalDate rescheduledDate = rescheduleHolidayDate(currentDate, endDate,
                        semestralSchedule.getScheduleGroupId(),
                        semestralSchedule.getStartTime(),
                        semestralSchedule.getEndTime(),
                        occupiedDays);

                if (rescheduledDate != null) {
                    // Crear horario reprogramado
                    DailySchedule dailySchedule = new DailySchedule(
                            scheduleId,
                            semestralSchedule.getScheduleGroupId(),
                            rescheduledDate,
                            semestralSchedule.getStartTime(),
                            semestralSchedule.getEndTime(),
                            rescheduledDate.getDayOfWeek().toString(),
                            semestralSchedule.getCapacity()
                    );

                    String holidayDescription = optionalHoliday.isPresent() ?
                            optionalHoliday.get().getName() : "Día festivo";

                    dailySchedule.setRescheduled(true);
                    dailySchedule.setOriginalDate(currentDate);
                    dailySchedule.setHolidayDescription(holidayDescription);

                    generatedSchedules.add(dailySchedule);
                } else {
                    // No se pudo reprogramar, crear un horario marcado como festivo
                    DailySchedule dailySchedule = new DailySchedule(
                            scheduleId,
                            semestralSchedule.getScheduleGroupId(),
                            currentDate,
                            semestralSchedule.getStartTime(),
                            semestralSchedule.getEndTime(),
                            currentDate.getDayOfWeek().toString(),
                            semestralSchedule.getCapacity()
                    );

                    String holidayDescription = optionalHoliday.isPresent() ?
                            optionalHoliday.get().getName() : "Día festivo";

                    dailySchedule.setHoliday(true);
                    dailySchedule.setHolidayDescription(holidayDescription);

                    generatedSchedules.add(dailySchedule);
                }
            } else {
                // Si no es festivo, crear un horario diario normal
                DailySchedule dailySchedule = new DailySchedule(
                        scheduleId,
                        semestralSchedule.getScheduleGroupId(),
                        currentDate,
                        semestralSchedule.getStartTime(),
                        semestralSchedule.getEndTime(),
                        currentDate.getDayOfWeek().toString(),
                        semestralSchedule.getCapacity()
                );

                generatedSchedules.add(dailySchedule);
            }

            // Avanzar a la siguiente semana (7 días)
            currentDate = currentDate.plusDays(7);
        }

        // Guardar todos los horarios diarios generados
        return dailyScheduleRepository.saveAll(generatedSchedules);
    }

    /**
     * Obtiene los días de la semana ocupados por un grupo de horarios
     * @param scheduleGroupId ID del grupo de horarios
     * @return Conjunto de días de la semana ocupados
     */
    private Set<DayOfWeek> getOccupiedDaysOfWeekByGroupId(String scheduleGroupId) {
        List<GymSchedules> groupSchedules = gymScheduleMongoRepository.findByScheduleGroupId(scheduleGroupId);
        Set<DayOfWeek> occupiedDays = new HashSet<>();

        for (GymSchedules schedule : groupSchedules) {
            occupiedDays.add(DayOfWeek.valueOf(schedule.getDayOfWeek()));
        }

        return occupiedDays;
    }

    /**
     * Verifica si existe un horario para un grupo específico en una fecha, hora y capacidad determinada
     * @param scheduleGroupId ID del grupo de horarios
     * @param date Fecha a verificar
     * @param startTime Hora de inicio
     * @param endTime Hora de fin
     * @return true si existe un horario en ese día y hora para ese grupo
     */
    private boolean hasScheduleForDayAndTime(String scheduleGroupId, LocalDate date,
                                             LocalTime startTime, LocalTime endTime) {
        // Obtenemos los horarios semestrales para este grupo
        List<GymSchedules> groupSchedules = gymScheduleMongoRepository.findByScheduleGroupId(scheduleGroupId);

        // Verificamos si alguno coincide con el día de la semana
        String dayOfWeekStr = date.getDayOfWeek().toString();

        for (GymSchedules schedule : groupSchedules) {
            // Si coincide día de la semana y está dentro del periodo del semestre
            if (schedule.getDayOfWeek().equals(dayOfWeekStr) &&
                    !date.isBefore(schedule.getStartDate()) &&
                    !date.isAfter(schedule.getEndDate())) {

                // Verificamos si las horas se solapan
                boolean hasTimeOverlap =
                        // El horario existente comienza durante nuestro rango
                        (schedule.getStartTime().compareTo(startTime) >= 0 &&
                                schedule.getStartTime().compareTo(endTime) < 0) ||
                                // El horario existente termina durante nuestro rango
                                (schedule.getEndTime().compareTo(startTime) > 0 &&
                                        schedule.getEndTime().compareTo(endTime) <= 0) ||
                                // El horario existente abarca completamente nuestro rango
                                (schedule.getStartTime().compareTo(startTime) <= 0 &&
                                        schedule.getEndTime().compareTo(endTime) >= 0) ||
                                // Nuestro rango abarca completamente el horario existente
                                (startTime.compareTo(schedule.getStartTime()) <= 0 &&
                                        endTime.compareTo(schedule.getEndTime()) >= 0);

                if (hasTimeOverlap) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Intenta reprogramar un día festivo para el siguiente día hábil que no esté ocupado por el mismo grupo
     * @param holidayDate Fecha del día festivo
     * @param endDate Fecha límite para reprogramar
     * @param scheduleGroupId ID del grupo de horarios
     * @param startTime Hora de inicio
     * @param endTime Hora de fin
     * @param occupiedDays Conjunto de días de la semana ya ocupados por este grupo
     * @return Nueva fecha reprogramada o null si no es posible reprogramar
     */
    private LocalDate rescheduleHolidayDate(LocalDate holidayDate, LocalDate endDate,
                                            String scheduleGroupId, LocalTime startTime,
                                            LocalTime endTime, Set<DayOfWeek> occupiedDays) {
        // Intentar reprogramar hasta 10 días después (2 semanas)
        for (int i = 1; i <= 10; i++) {
            LocalDate candidateDate = holidayDate.plusDays(i);

            // Si se pasa de la fecha límite, no reprogramar
            if (candidateDate.isAfter(endDate)) {
                return null;
            }

            // Verificar que:
            // 1. No sea fin de semana
            // 2. No sea festivo
            // 3. No sea un día ya ocupado por el mismo grupo
            // 4. No exista otro horario del mismo grupo en ese día y hora
            if (candidateDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    candidateDate.getDayOfWeek() != DayOfWeek.SUNDAY &&
                    !holidayService.isHoliday(candidateDate) &&
                    !occupiedDays.contains(candidateDate.getDayOfWeek()) &&
                    !hasScheduleForDayAndTime(scheduleGroupId, candidateDate, startTime, endTime)) {

                return candidateDate;
            }
        }

        // Si no se encontró fecha para reprogramar
        return null;
    }

    @Override
    public List<DailySchedule> addUserToScheduleByGroup(String scheduleGroupId, String userId) throws Exception {
        List<DailySchedule> schedules = dailyScheduleRepository.findByScheduleGroupId(scheduleGroupId);

        if (schedules.isEmpty()) {
            throw new Exception("No se encontraron horarios para el grupo con ID " + scheduleGroupId);
        }

        boolean addedToAtLeastOne = false;

        for (DailySchedule schedule : schedules) {
            // Solo añadir si el usuario no está y hay capacidad
            if (!schedule.getUsers().contains(userId) && schedule.getCapacity() > 0) {
                schedule.getUsers().add(userId);
                schedule.setCapacity(schedule.getCapacity() - 1);
                schedule.updateStatus();
                dailyScheduleRepository.save(schedule);
                addedToAtLeastOne = true;
            }
        }

        if (!addedToAtLeastOne) {
            throw new Exception("El usuario ya estaba en todos los horarios o no hay cupos disponibles.");
        }

        return schedules;
    }

    @Override
    public DailySchedule addUserToSchedule(String id, String userId) throws Exception {
        Optional<DailySchedule> optionalSchedule = dailyScheduleRepository.findById(id);

        if (!optionalSchedule.isPresent()) {
            throw new Exception("No se encontró el horario con ID " + id);
        }

        DailySchedule schedule = optionalSchedule.get();

        // Verificar si el usuario ya está en la lista
        if (schedule.getUsers().contains(userId)) {
            throw new Exception("El usuario ya está registrado en este horario");
        }

        // Verificar si hay capacidad disponible
        if (schedule.getCapacity() <= 0) {
            throw new Exception("No hay cupos disponibles en este horario");
        }

        // Verificar si el usuario ya tiene 3 reservas en la misma semana
        if (hasMaxWeeklyReservations(userId, schedule.getDate())) {
            throw new Exception("Has alcanzado el límite máximo de 3 reservas por semana");
        }

        // Añadir usuario a la lista
        schedule.getUsers().add(userId);

        // Reducir la capacidad
        schedule.setCapacity(schedule.getCapacity() - 1);

        // Actualizar el estado
        schedule.updateStatus();

        // Guardar y devolver el horario actualizado
        return dailyScheduleRepository.save(schedule);
    }

    private boolean hasMaxWeeklyReservations(String userId, LocalDate date) {
        // Calcular el primer día (lunes) y último día (domingo) de la semana
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Buscar todos los horarios en esa semana donde el usuario esté inscrito
        List<DailySchedule> userSchedulesInWeek = dailyScheduleRepository.findByDateBetweenAndUsersContaining(
                startOfWeek, endOfWeek, userId);

        // Si ya tiene 3 o más reservas, devolver true
        return userSchedulesInWeek.size() >= 3;
    }


    @Override
    public DailySchedule removeUserFromScheduleById(String id, String userId) throws Exception {
        Optional<DailySchedule> optionalSchedule = dailyScheduleRepository.findById(id);

        if (!optionalSchedule.isPresent()) {
            throw new Exception("No se encontró el horario con ID " + id);
        }

        DailySchedule schedule = optionalSchedule.get();

        // Verificar si el usuario está en la lista
        if (!schedule.getUsers().contains(userId)) {
            throw new Exception("El usuario no está registrado en este horario");
        }

        // Eliminar el usuario de la lista
        schedule.getUsers().remove(userId);

        // Incrementar la capacidad
        schedule.setCapacity(schedule.getCapacity() + 1);

        // Actualizar el estado
        schedule.updateStatus();

        // Guardar y devolver el horario actualizado
        return dailyScheduleRepository.save(schedule);
    }

    @Override
    public List<DailySchedule> removeUserFromScheduleByDate(LocalDate date, String userId) throws Exception {
        // Buscar todos los horarios en esa fecha (necesitaremos añadir este método al repositorio)
        List<DailySchedule> schedules = dailyScheduleRepository.findByDate(date);

        if (schedules.isEmpty()) {
            throw new Exception("No se encontraron horarios para la fecha " + date);
        }

        List<DailySchedule> updatedSchedules = new ArrayList<>();
        boolean removedFromAtLeastOne = false;

        for (DailySchedule schedule : schedules) {
            // Verificar si el usuario está en la lista
            if (schedule.getUsers().contains(userId)) {
                // Eliminar el usuario de la lista
                schedule.getUsers().remove(userId);

                // Incrementar la capacidad
                schedule.setCapacity(schedule.getCapacity() + 1);

                // Actualizar el estado
                schedule.updateStatus();

                // Guardar el horario actualizado
                updatedSchedules.add(dailyScheduleRepository.save(schedule));
                removedFromAtLeastOne = true;
            }
        }

        if (!removedFromAtLeastOne) {
            throw new Exception("El usuario no está registrado en ningún horario para la fecha " + date);
        }

        return updatedSchedules;
    }


    @Override
    public List<DailySchedule> findIncompleteSchedules() {
        return dailyScheduleRepository.findByStatus("INCOMPLETE");
    }


    @Override
    public List<DailySchedule> findAll() {
        return dailyScheduleRepository.findAll();
    }

    @Override
    public Optional<DailySchedule> findById(String id) {
        return dailyScheduleRepository.findById(id);
    }

    @Override
    public List<DailySchedule> findByScheduleId(String scheduleId) {
        return dailyScheduleRepository.findByScheduleId(scheduleId);
    }

    @Override
    public List<DailySchedule> findByScheduleGroupId(String scheduleGroupId) {
        return dailyScheduleRepository.findByScheduleGroupId(scheduleGroupId);
    }

}