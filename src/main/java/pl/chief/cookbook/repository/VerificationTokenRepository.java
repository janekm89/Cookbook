package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.chief.cookbook.verification.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    VerificationToken findByToken(String token);
    @Query(value = "select id, token, expiryDate from VerificationToken where user.id = :userId")
    VerificationToken findByUserId(@Param(value = "userId") int userId);
}
