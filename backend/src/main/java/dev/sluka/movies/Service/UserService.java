package dev.sluka.movies.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.sluka.movies.DTO.UserDTO;
import dev.sluka.movies.Entity.Role;
import dev.sluka.movies.Entity.User;
import dev.sluka.movies.Repository.RoleRepository;
import dev.sluka.movies.Repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager  authenticationManager;
    private final JwtService jwtService;
    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 1 * 60 * 60 * 1000;
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if(userRepository.existsByUserName(userDTO.getUserName())){
            throw new RuntimeException("Username is already used");
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
    
        Set<Role> roles = new HashSet<>();
    
        // ✅ If userDTO.getRoles() is null or empty, assign default "USER" role
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER");
            if (defaultRole == null) {
                throw new RuntimeException("Default USER role not found in the database");
            }
            roles.add(defaultRole);
        } else {
            for (String roleName : userDTO.getRoles()) {
                Role role = roleRepository.findByName(roleName.toUpperCase());
                if (role == null) {
                    throw new RuntimeException("Role " + roleName + " not found in the database");
                }
                roles.add(role);
            }
        }
    
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
    
        // Convert User to UserDTO before returning
        return new UserDTO(savedUser);
    }

    @Transactional
    public void deleteUser(String userName) {
        User user = userRepository.findByUserName(userName);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
    
        userRepository.deleteByUserName(userName);
    }


   


    
    
    @Transactional
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        }
        
        


    private UserDTO convertToDTO(User user) {
        // Map relevant fields from User to UserDTO
        return new UserDTO(user);
    }


    public User registerUserWithRole(String username, String password, String roleName) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
    
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RuntimeException("Role " + roleName + " not found in the database");
        }
    
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    
        return userRepository.save(user);
    }
    
   
    public String verify(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName());
    
        if (user == null) {
            throw new RuntimeException("User not found");
        }
    
        if (!user.isAccountNonLocked()) {
            if (unlockWhenTimeExpired(user)) {
                return "Account unlocked. Please try logging in again.";
            } else {
                return "Your account is locked. Try again in 1 hour.";
            }
        }
    
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), userDTO.getPassword())
            );
    
            resetFailedAttempts(user.getUserName());
    
            String accessToken = jwtService.generateToken(new UserDTO(user));
            String refreshToken = jwtService.generateRefreshToken(new UserDTO(user));

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", accessToken);
            tokenMap.put("refreshToken", refreshToken);
            tokenMap.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(",")));

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(tokenMap);
        } catch (Exception e) {
            increaseFailedAttempts(user);
    
            if (user.getFailedAttempt() >= MAX_FAILED_ATTEMPTS) {
                lock(user);
                return "Too many failed attempts. Your account is locked for 1 hour.";
            }
    
            return "Invalid credentials. Attempt " + user.getFailedAttempt() + "/3.";
        }
    }
    

    public User getByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User getByUserName(String username){
        User user = userRepository.findByUserName(username);
        return user;
    }

    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.save(user);
    
        userRepository.delete(user);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getUserName());
    }
     
    public void resetFailedAttempts(String username) {
        repo.updateFailedAttempts(0, username);
    }
     @Transactional
    public void lock(User user) {
       Timestamp lockTimestamp = new Timestamp(System.currentTimeMillis());

        int updatedRows = userRepository.lockUser(lockTimestamp, user.getUserName());

    if (updatedRows == 0) {
        throw new RuntimeException("Locking failed for user: " + user.getUserName());
    }
    }

    public boolean unlockWhenTimeExpired(User user) {
        if (user.getLockTime() == null) return false; 
    
        long lockTimeMillis = user.getLockTime().getTime();
        long currentTimeMillis = System.currentTimeMillis();
    
        if (lockTimeMillis + LOCK_TIME_DURATION < currentTimeMillis) {
            userRepository.unlockUser(user.getUserName()); 
            System.out.println("User " + user.getUserName() + " unlocked automatically.");
            return true;
        }
        return false;
    }
    
    @Transactional
    public void updateUserPassword(String username, String newPassword) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
    
        String hashedPassword = bCryptPasswordEncoder.encode(newPassword);
       userRepository.updatePassword(username, hashedPassword);
    }
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
   
}
