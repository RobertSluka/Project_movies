package dev.sluka.movies.DTO;

import dev.sluka.movies.Entity.Role;
import dev.sluka.movies.Entity.User;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    // private int id;
    private String userName;
    private String password;
    private String email;
    private Set<String> roles;

    // Default constructor
    public UserDTO() {
    }

    // Constructor to convert User entity to UserDTO
    public UserDTO(User user) {
        // this.id = user.getId();
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles() != null
                ? user.getRoles().stream()
                        .map(Role::getName) 
                        .collect(Collectors.toSet())
                : new HashSet<>(); 
    }
}