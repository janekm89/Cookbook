package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.UserEmailNotFoundException;
import pl.chief.cookbook.exception.UserLoginNotFoundException;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) throws EntityAlreadyExistException {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EntityAlreadyExistException(user.getEmail());
        if(userRepository.findByLogin(user.getLogin()).isPresent())
            throw new EntityAlreadyExistException(user.getLogin());
        userRepository.save(user);
    }

    public User loadUserByLogin(String login) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (!userOpt.isPresent()) {
            throw new UserLoginNotFoundException(login);
        } else return userOpt.get();
    }

    public User loadUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new UserEmailNotFoundException(email);
        } else return userOpt.get();
    }
}
