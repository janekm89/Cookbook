package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.UserEmailNotFoundException;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.repository.UserRepository;
import pl.chief.cookbook.repository.VerificationTokenRepository;
import pl.chief.cookbook.security.PasswordEncoderConfiguration;
import pl.chief.cookbook.verification.VerificationToken;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordEncoderConfiguration encoder;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoderConfiguration encoder) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
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

    public User findUserByToken(String verificationToken){
        return userRepository.findByToken(verificationToken).orElseThrow(EntityNotFoundException::new);
    }

    public void createVerificationToken(User user, String token){
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(String verificationToken){
        return verificationTokenRepository.findByToken(verificationToken);
    }

    public VerificationToken findVerificationTokenByUserId(int userId){
        return verificationTokenRepository.findByUserId(userId);
    }

    public void removeUser(User user){
        userRepository.delete(user);
    }

    public void activateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setActive(1);
        userRepository.save(existingUser);
    }
}
