package dev.sluka.movies.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.sluka.movies.DTO.PasswordUpdateDTO;
import dev.sluka.movies.DTO.UserDTO;
import dev.sluka.movies.Entity.Role;
import dev.sluka.movies.Entity.User;
import dev.sluka.movies.Entity.UserRole;
import dev.sluka.movies.Repository.UserRepository;
import dev.sluka.movies.Service.CustomUserDetailsService;
import dev.sluka.movies.Service.JwtService;
import dev.sluka.movies.Service.RoleService;
import dev.sluka.movies.Service.UserService;
import jakarta.validation.Valid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserController {

private final UserService userService;

private final UserRepository userRepository;

private final RoleService roleService;

private final JwtService jwtService;
private final CustomUserDetailsService customUserDetailsService;

public UserController(UserRepository userRepository, UserService userService, JwtService jwtService,CustomUserDetailsService
customUserDetailsService,RoleService roleService) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
    this.roleService = roleService;
}
@GetMapping("/whoami")
public ResponseEntity<?> whoami(Authentication authentication) {
    System.out.println("Logged in user: " + authentication.getName());
    System.out.println("Authorities: " + authentication.getAuthorities());
    return ResponseEntity.ok(authentication);
}

@GetMapping("/users/all")
public List<UserDTO> getAllUsers() {
    return userService.getAllUsers();
}


@GetMapping("/user/roles/all")
public List<UserRole> getAllUserRoles() {
    return roleService.getAllUserRoles();
}

@GetMapping("/roles/all")
public List<Role> getAllRoles() {
    return userService.getAllRoles();
}

@PostMapping("/register")
public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDto) {
    UserDTO registeredUser = userService.registerUser(userDto);
    return ResponseEntity.ok(registeredUser);
}

@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO user) {
    String tokens = userService.verify(user);
    
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        Map<String, String> responseMap = objectMapper.readValue(tokens, Map.class);
        return ResponseEntity.ok(responseMap);
    } catch (JsonProcessingException e) {
        return ResponseEntity.status(500).body(Map.of("error", "Error processing response"));
    }
}

@PostMapping("/refresh-token")
public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");

    try {
        String username = jwtService.extractUserName(refreshToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
        }

        User user = userRepository.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        String newAccessToken = jwtService.generateToken(new UserDTO(user));
        String newRefreshToken = jwtService.generateRefreshToken(new UserDTO(user));

        return ResponseEntity.ok(Map.of(
            "accessToken", newAccessToken,
            "refreshToken", newRefreshToken
        ));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", "Error refreshing token"));
    }
}

@PatchMapping("/user/password")
public ResponseEntity<String> updateUserPassword(@RequestBody @Valid PasswordUpdateDTO request, Principal principal) {

    String username = principal.getName();
    userService.updateUserPassword(username,request.getNewPassword());
    return ResponseEntity.ok("Password updated successfully");
}

@DeleteMapping("/admin/delete/{username}")
public ResponseEntity<String> deleteUser(@PathVariable String username, Principal principal) {

    userService.deleteUser(username);
    return ResponseEntity.ok("User deleted successfully");
}
@DeleteMapping("/admin/remove-roles/{username}")
public ResponseEntity<?> removeUserRoles(@PathVariable String username) {
    try {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        
        user.getRoles().clear();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
// @DeleteMapping("/admin/delete/{userId}")
// public ResponseEntity<String> deleteUser(@PathVariable int userId) {
//         userService.deleteUser(userId);
//         return ResponseEntity.ok("User deleted successfully");
//     }

// @PostMapping("/admin/register")
// public User registerAdmin(@RequestBody UserDTO user, @RequestParam String roleName) {
//         return userService.registerUserWithRole(user.getUserName(),user.getPassword(), roleName);
//     }
}
