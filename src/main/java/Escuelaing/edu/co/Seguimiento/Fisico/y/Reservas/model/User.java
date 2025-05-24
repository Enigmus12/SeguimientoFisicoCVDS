package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.List;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserDTO;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
@Document(collection = "users")
public class User {
    @MongoId
    private String id;
    private String userName;
    private Integer numberId;
    private String role;
    private String password;
    private String passwordConfirmation;

    @PersistenceCreator
    public User(String id, String userName ,Integer numberId, String role ,String password, String passwordConfirmation) {
        this.id = id;
        this.userName = userName;
        this.numberId = numberId;
        this.role = role;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public User(UserDTO userDTO) {
        this.userName = userDTO.getUserName();
        this.numberId = userDTO.getNumberId();
        this.role = userDTO.getRole();
        this.password = userDTO.getPassword();
        this.passwordConfirmation = userDTO.getPasswordConfirmation();
    }

}
