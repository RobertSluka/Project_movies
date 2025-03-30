package dev.sluka.movies.Controller;

import java.security.Principal;
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
import dev.sluka.movies.Entity.User;
import dev.sluka.movies.Repository.UserRepository;
import dev.sluka.movies.Service.CustomUserDetailsService;
import dev.sluka.movies.Service.JwtService;
import dev.sluka.movies.Service.UserService;
import jakarta.validation.Valid;

@RestController
public class UserController {

private final UserService userService;

private final UserRepository userRepository;

private final JwtService jwtService;
private final CustomUserDetailsService customUserDetailsService;

public UserController(UserRepository userRepository, UserService userService, JwtService jwtService,CustomUserDetailsService
customUserDetailsService) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
}
@GetMapping("/whoami")
public ResponseEntity<?> whoami(Authentication authentication) {
    System.out.println("Logged in user: " + authentication.getName());
    System.out.println("Authorities: " + authentication.getAuthorities());
    return ResponseEntity.ok(authentication);
}

@PostMapping("/register")
public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDto) {
    UserDTO registeredUser = userService.registerUser(userDto);
    return ResponseEntity.ok(registeredUser);
}



@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody UserDTO user) {
  
    String tokens = userService.verify(user);
    return ResponseEntity.ok(tokens);
  
}

// @PostMapping("/refresh-token")
// public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
//     String refreshToken = request.get("refreshToken");
//     String username = request.get("username");

//     UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

//     if (!jwtService.isTokenValid(refreshToken, userDetails)) {
//         return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
//     }
    

//     User user = userRepository.findByUserName(username);

//     if (user == null) {
//         return ResponseEntity.status(404).body(Map.of("error", "User not found"));
//     }

//     String newAccessToken = jwtService.generateToken(new UserDTO(user));

//     return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
// }


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
