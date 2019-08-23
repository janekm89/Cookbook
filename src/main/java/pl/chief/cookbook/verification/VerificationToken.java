package pl.chief.cookbook.verification;

import lombok.Getter;
import pl.chief.cookbook.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Date expiryDate;

    public VerificationToken() {
        this.expiryDate = calculateDateExpiry(EXPIRATION);
    }

    public VerificationToken(String token, User user) {
        this();
        this.token = token;
        this.user = user;
    }

    private Date calculateDateExpiry(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }


}
