package pl.chief.cookbook.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @NotEmpty(message = "*Please provide your username")
    @Column(unique = true)
    private String username;
    private String surname;
    @NotEmpty(message = "*Please provide a valid Email")
    @Email
    @Column(unique = true)
    private String email;
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    private String role;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Recipe> recipes;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Ingredient> ingredients;
    private int active;

    public User() {
        recipes = new ArrayList<>();
        ingredients = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.
                commaSeparatedStringToAuthorityList(this.role);
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (active == 1)
            return true;
        else return false;
    }
}
