package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config.UserServiceException;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.AuthenticationResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserAuthenticationDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;

import java.util.List;

public interface UserService {
    User getUser(String userId) throws UserServiceException;
    User saveUser(UserDTO user);
    List<User> getAllUsers();
    void deleteUser(String userId) throws UserServiceException;
    AuthenticationResponseDTO authenticate(UserAuthenticationDTO authenticationDTO);
    User getUserByNumberId(Integer numberId) throws UserServiceException;
}
