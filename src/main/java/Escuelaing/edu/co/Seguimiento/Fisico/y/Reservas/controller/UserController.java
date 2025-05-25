package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.controller;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config.UserServiceException;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.AuthenticationResponseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserAuthenticationDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.UserDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-service")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public List<User> users(){
        return userService.getAllUsers();
    }


    @GetMapping("/users/{userId}")
    public User user (@PathVariable String userId) throws UserServiceException {
        return userService.getUser(userId);
    }

    @PostMapping("/register")
    public User user(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }


    @DeleteMapping("/users/{userId}")
    public List<User> deleteUser(@PathVariable String userId) throws UserServiceException {
        userService.deleteUser(userId);
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO authenticate(@RequestBody UserAuthenticationDTO authenticationDTO) {
        return userService.authenticate(authenticationDTO);
    }

    @GetMapping("/users/by-number-id/{numberId}")
    public User getUserByNumberId(@PathVariable Integer numberId) throws UserServiceException {
        return userService.getUserByNumberId(numberId);
    }

}
