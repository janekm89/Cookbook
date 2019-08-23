package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.chief.cookbook.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    @Query(value = "select u.id, u.username, u.name, u.surname, u.email, u.password, u.active, u.role from " +
            "user u join verification_token vt on u.id = vt.user_id where token = :token", nativeQuery = true)
    Optional<User> findByToken(@Param(value = "token") String token);

}
