package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.chief.cookbook.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

}
