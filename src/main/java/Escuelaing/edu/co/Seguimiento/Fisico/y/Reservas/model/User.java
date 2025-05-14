package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.List;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
@Document(collection = "users")
@ToString
public class User {
    @Id
    private String userId;
    private Integer numberId;
    private String role;
    private String password;
    private String passwordConfirmation;

    @PersistenceCreator
    public User(String userId, Integer numberId, String role ,String password, String passwordConfirmation) {
        this.userId = userId;
        this.numberId = numberId;
        this.role = role;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public User(UserDTO userDTO) {
        this.userId = userDTO.getUserId();
        this.numberId = userDTO.getNumberId();
        this.role = userDTO.getRole();
        this.password = userDTO.getPassword();
        this.passwordConfirmation = userDTO.getPasswordConfirmation();
    }

}