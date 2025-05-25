package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.impl;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config.UserServiceException;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model.User;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Mongo.UserMongoRepository;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.service.interfaces.Service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserMongoRepository userMongoRepository;

    @Override
    public void save(User user) {
        userMongoRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll();
    }

    @Override
    public User findById(String userId) throws UserServiceException {
        Optional<User> user = userMongoRepository.findById(userId);
        if(user.isEmpty()) throw new UserServiceException("User Not found");
        return user.get();
    }

    @Override
    public void deleteById(String userId) throws UserServiceException {
        userMongoRepository.deleteById(userId);
    }

    @Override
    public User findByUserName(String userName) throws UserServiceException {
        User user = userMongoRepository.findByUserName(userName);
        if (user == null) throw new UserServiceException("User Not found");
        return user;
    }

    @Override
    public User findByNumberId(Integer numberId) throws UserServiceException {
        User user = userMongoRepository.findByNumberId(numberId);
        if (user == null) throw new UserServiceException("User Not found");
        return user;
    }

}
