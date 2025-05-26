package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Holiday;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.HolidayService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.HolydayMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolydayMongoRepository holidayRepository;

    @Override
    public void initializeHolidays() {
        // Solo inicializa si no hay festivos en la base de datos
        if (holidayRepository.count() == 0) {
            // Festivos 2025 de Colombia
            // Aquí defines manualmente todos los festivos
            holidayRepository.save(new Holiday(LocalDate.of(2025, 1, 1), "Año Nuevo", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 1, 6), "Día de los Reyes Magos", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 3, 24), "Día de San José", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 4, 17), "Jueves Santo", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 4, 18), "Viernes Santo", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 5, 1), "Día del Trabajo", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 5, 26), "Día de la Ascensión", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 6, 16), "Corpus Christi", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 6, 23), "Sagrado Corazón", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 6, 30), "San Pedro y San Pablo", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 7, 20), "Día de la Independencia", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 8, 7), "Batalla de Boyacá", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 8, 18), "Asunción de la Virgen", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 10, 13), "Día de la Raza", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 11, 3), "Todos los Santos", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 11, 17), "Independencia de Cartagena", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 12, 8), "Día de la Inmaculada Concepción", "Festivo Nacional"));
            holidayRepository.save(new Holiday(LocalDate.of(2025, 12, 25), "Navidad", "Festivo Nacional"));
        }
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    @Override
    public Optional<Holiday> getHolidayById(String id) {
        return holidayRepository.findById(id);
    }

    @Override
    public Optional<Holiday> getHolidayByDate(LocalDate date) {
        // Buscar todos los festivos y comparar solo la parte de fecha (ignorando hora/zona)
        return holidayRepository.findAll().stream()
            .filter(h -> h.getDate() != null && h.getDate().isEqual(date))
            .findFirst();
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.findByDate(date).isPresent();
    }
}