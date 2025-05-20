package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayService {
    void initializeHolidays();
    List<Holiday> getAllHolidays();
    Optional<Holiday> getHolidayById(String id);
    Optional<Holiday> getHolidayByDate(LocalDate date);
    boolean isHoliday(LocalDate date);
}