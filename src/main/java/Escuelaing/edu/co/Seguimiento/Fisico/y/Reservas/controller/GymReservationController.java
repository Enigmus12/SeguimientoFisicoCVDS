package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;


import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymReservation;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.GymSession;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymReservationService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.GymSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservation-service")
public class GymReservationController {

    @Autowired
    private GymReservationService reservationService;

    @Autowired
    private GymSessionService sessionService;

    @PostMapping("/reservations")
    public GymReservation reserveSession(@RequestBody GymReservation reservation) {
        return null;
    }

    @DeleteMapping("/reservations/{reservationId}")
    public void cancelReservation(@PathVariable String reservationId) {
        reservationService.cancelReservation(reservationId);
    }

    @GetMapping("/sessions/date")
    public List<GymSession> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return sessionService.getSessionsByDate(date);
    }
}
