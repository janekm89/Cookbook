package pl.chief.cookbook.builder;

import pl.chief.cookbook.model.User;

public class UserBuilder {
    private User user;

    public UserBuilder(){
        this.user = new User();
    }

    public UserBuilder withUsername(String username){
        user.setUsername(username);
        return this;
    }
    public UserBuilder withName(String name){
        user.setName(name);
        return this;
    }
    public UserBuilder withSurname(String surname){
        user.setSurname(surname);
        return this;
    }
    public UserBuilder withEmail(String email){
        user.setEmail(email);
        return this;
    }
    public UserBuilder withPassword(String password){
        user.setPassword(password);
        return this;
    }
    public UserBuilder withRole(String role){
        user.setRole(role);
        return this;
    }
    public UserBuilder withRoleAdmin(){
        return withRole("ADMIN");
    }
    public UserBuilder withRoleUser(){
        return withRole("USER");
    }
    public UserBuilder withActivation(int active){
        user.setActive(active);
        return this;
    }
    public UserBuilder activated(){
        return withActivation(1);
    }
    public UserBuilder unactive(){
        return withActivation(0);
    }
    public User create(){
        return user;
    }
}
