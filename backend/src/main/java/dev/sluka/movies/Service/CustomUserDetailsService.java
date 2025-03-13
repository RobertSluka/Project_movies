package dev.sluka.movies.Service;


import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import dev.sluka.CustomUserdetails;
import dev.sluka.movies.Entity.User;
import dev.sluka.movies.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //  @Autowired
    // private LoginAttemptService loginAttemptService;
 
    // @Autowired
    // private HttpServletRequest request;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     User user = userRepository.findByUserName(username);
    //     if (Objects.isNull(user)) {
    //         System.out.println("User not available");
    //         throw new UsernameNotFoundException("User not found");
    //     }
    //     return new CustomUserdetails(user);
    // } 

        @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
            // return new org.springframework.security.core.userdetails.User(
            //     " ", " ", true, true, true, true, 
            //     getAuthorities(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
        }
        // if (loginAttemptService.isBlocked()) {
        //     throw new RuntimeException("blocked");
        // }

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Prefix roles with "ROLE_"
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }

    
}