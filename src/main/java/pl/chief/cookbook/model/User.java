package pl.chief.cookbook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import pl.chief.cookbook.features.UserRole;

import javax.persistence.*;
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
    private String name;
    private String login;
    private String surname;
    private String email;
    private String password;
    @Enumerated(EnumType.ORDINAL)
    private UserRole userRole;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Recipe> recipes;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Ingredient> ingredients;

    public User() {
        recipes = new ArrayList<>();
        ingredients = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.
                commaSeparatedStringToAuthorityList(this.userRole.toString());
    }

    @Override
    public String getUsername() {
        return this.login;
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
        //TODO account activation after clicking on link in email received
        return true;
    }
}
