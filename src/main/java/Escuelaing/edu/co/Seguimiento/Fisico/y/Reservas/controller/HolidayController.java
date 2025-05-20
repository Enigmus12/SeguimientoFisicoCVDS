package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.Holiday;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @GetMapping
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Holiday> getHolidayById(@PathVariable String id) {
        return holidayService.getHolidayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date")
    public ResponseEntity<Holiday> getHolidayByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return holidayService.getHolidayByDate(date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/is-holiday")
    public ResponseEntity<Boolean> isHoliday(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(holidayService.isHoliday(date));
    }

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeHolidays() {
        holidayService.initializeHolidays();
        return new ResponseEntity<>("Festivos inicializados correctamente", HttpStatus.OK);
    }
}