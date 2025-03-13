// package dev.sluka.movies.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.security.authentication.LockedException;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
// import org.springframework.stereotype.Component;

// import dev.sluka.movies.Entity.User;
// import dev.sluka.movies.Service.UserService;
// import io.jsonwebtoken.io.IOException;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
     
//     @Autowired
//     @Lazy
//     private UserService userService;
     
//     @Override
//     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                         AuthenticationException exception) throws IOException, ServletException {
//         // Extract username from exception message
//         String userName = request.getParameter("userName"); // ✅ First, try to get it from request
//         if (userName == null && exception.getMessage() != null) {
//             userName = extractUsernameFromException(exception);
//         }
    
//         if (userName != null) {
//             User user = userService.getByUserName(userName);
//             if (user != null) {
//                 if (user.isEnabled() && user.isAccountNonLocked()) {
//                     if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
//                         userService.increaseFailedAttempts(user);
//                     } else {
//                         userService.lock(user);
//                         exception = new LockedException("Your account has been locked due to 3 failed attempts."
//                                 + " It will be unlocked after 24 hours.");
//                     }
//                 } else if (!user.isAccountNonLocked()) {
//                     if (userService.unlockWhenTimeExpired(user)) {
//                         exception = new LockedException("Your account has been unlocked. Please try to login again.");
//                     }
//                 }
//             }
//         }
    
//         super.setDefaultFailureUrl("/login?error");
//         super.onAuthenticationFailure(request, response, exception);
//     }
    
//     /**
//      * Extracts the username from the AuthenticationException message
//      */
//     private String extractUsernameFromException(AuthenticationException exception) {
//         if (exception.getMessage() != null && exception.getMessage().contains("Bad credentials")) {
//             // Example error message: "Bad credentials for user 'robo'"
//             String[] parts = exception.getMessage().split("'");
//             if (parts.length > 1) {
//                 return parts[1]; // Extracts 'robo' from "Bad credentials for user 'robo'"
//             }
//         }
//         return null;
//     }
// }    