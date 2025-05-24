package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config.UserServiceException;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.AuthenticationResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserAuthenticationDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.UserRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.UserService;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User getUser(String userId) throws UserServiceException {
        return userRepository.findById(userId);
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) throws UserServiceException {
        userRepository.deleteById(userId);
    }

    @Override
    public AuthenticationResponseDTO authenticate(UserAuthenticationDTO authenticationDTO) {
        try {
            // Buscar usuario por nombre de usuario
            User user = userRepository.findByUserName(authenticationDTO.getUserName());

            // Verificar la contraseña
            if (user.getPassword().equals(authenticationDTO.getPassword())) {
                // Generar token JWT con el userId y el role
                String token = jwtUtil.generateToken(user.getId(), user.getRole());
                return new AuthenticationResponseDTO(true, user, token, "Autenticación exitosa");
            } else {
                return new AuthenticationResponseDTO(false, null, null, "Contraseña incorrecta");
            }
        } catch (UserServiceException e) {
            // Usuario no encontrado
            return new AuthenticationResponseDTO(false, null, null, "Usuario no encontrado");
        }
    }
}
