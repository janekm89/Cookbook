package pl.chief.cookbook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.chief.cookbook.service.UserService;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private DataSource dataSource;
    private PasswordEncoderConfiguration encoder;

    @Autowired
    public SecurityConfiguration(UserService userService, DataSource dataSource, PasswordEncoderConfiguration encoder) {
        this.userService = userService;
        this.dataSource = dataSource;
        this.encoder = encoder;
    }

    @Autowired
    private void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.inMemoryAuthentication()
                .withUser("user2")
                .password(encoder.passwordEncoder().encode("pass"))
                .roles("USER");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, active from user where username=?")
                .authoritiesByUsernameQuery("select username, role from user where username=?")
                .passwordEncoder(encoder.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable()


                // Register our CustomRequestCache, that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache())

                // Restrict access to our application.
                .and().authorizeRequests()

                .antMatchers("/img/**").permitAll()
                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                .anyRequest().authenticated()
                // Allow all requests by logged in users.
                // .anyRequest().authenticated()

                // Configure the login page.
                .and().formLogin().loginPage("/login").permitAll().loginProcessingUrl("/login")
                .failureUrl("/login")

                // Configure logout
                .and().logout().logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/icons/**",
                "/images/**",
                "/frontend/**",
                "/webjars/**",
                "/h2-console/**",
                "/frontend-es5/**", "/frontend-es6/**");
    }
}

