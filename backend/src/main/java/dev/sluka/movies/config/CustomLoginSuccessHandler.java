// package dev.sluka.movies.config;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import dev.sluka.CustomUserdetails;
// import dev.sluka.movies.Entity.User;
// import dev.sluka.movies.Repository.UserRepository;
// import dev.sluka.movies.Service.UserService;
// import io.jsonwebtoken.io.IOException;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;


// @Component
// public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//     @Autowired
//     @Lazy
//     private UserService userService; // Ensure this service exists and has the resetFailedAttempts method

//     @Autowired
//     private UserRepository userRepository;
//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                         Authentication authentication) throws IOException, ServletException, java.io.IOException {
//         Object principal = authentication.getPrincipal();
    
//         if (principal instanceof org.springframework.security.core.userdetails.User) { // ✅ Check correct type
//             org.springframework.security.core.userdetails.User userDetails =
//                     (org.springframework.security.core.userdetails.User) principal;
    
//             // Since we don't have direct access to `User`, fetch it from the database
//             User user = userRepository.findByUserName(userDetails.getUsername());
    
//             if (user != null && user.getFailedAttempt() > 0) {
//                 userService.resetFailedAttempts(user.getUserName()); // ✅ Reset failed attempts
//             }
//         }
    
//         super.onAuthenticationSuccess(request, response, authentication);
//     }
    
// }
