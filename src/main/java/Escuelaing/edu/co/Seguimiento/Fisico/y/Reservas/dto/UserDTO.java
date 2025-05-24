package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userName;
    private Integer numberId;
    private String role;
    private String password;
    private String passwordConfirmation;

}
