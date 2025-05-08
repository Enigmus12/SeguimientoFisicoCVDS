package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;
import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private boolean authenticated;
    private User user;
    private String token;
    private String message;

    public AuthenticationResponseDTO(boolean authenticated, User user, String token, String message) {
        this.authenticated = authenticated;
        this.user = user;
        this.token = token;
        this.message = message;
    }
}
