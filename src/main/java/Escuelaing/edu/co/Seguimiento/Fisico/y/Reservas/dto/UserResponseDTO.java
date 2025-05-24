package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String userName;
    private Integer numberId;
    private String role;
    // Omitimos campos sensibles como password y passwordConfirmation
}
