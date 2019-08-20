package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.UserEmailNotFoundException;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.repository.UserRepository;
import pl.chief.cookbook.security.PasswordEncoderConfiguration;
import pl.chief.cookbook.security.SecurityConfiguration;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoderConfiguration encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderConfiguration encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void addUser(User user) throws EntityAlreadyExistException {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EntityAlreadyExistException(user.getEmail());
        if(userRepository.findByUsername(user.getUsername()).isPresent())
            throw new EntityAlreadyExistException(user.getUsername());
        user.setRole("ROLE_" + user.getRole());
        user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }


    public User findUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new UserEmailNotFoundException(email);
        } else return userOpt.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("User with email " + username + " not found.");
        } else return userOpt.get();
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("User with email " + username + " not found.");
        } else return userOpt.get();
    }
}
