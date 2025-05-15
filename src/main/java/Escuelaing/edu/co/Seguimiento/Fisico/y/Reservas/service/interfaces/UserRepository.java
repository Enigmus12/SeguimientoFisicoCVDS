package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config.UserServiceException;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;

import java.util.List;

public interface UserRepository{
    void save(User user);
    List<User> findAll();
    User findById(String userId) throws UserServiceException;
    void deleteById(String userId) throws UserServiceException;
}