package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancellationRequestDTO {
    private String reservationId;
    private String cancellationReason;
}