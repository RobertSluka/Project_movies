// package dev.sluka.movies.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cglib.core.internal.LoadingCache;
// import org.springframework.stereotype.Service;

// import jakarta.servlet.http.HttpServletRequest;

// @Service
// public class LoginAttemptService {

//     public static final int MAX_ATTEMPT = 10;
//     private LoadingCache<String, Integer> attemptsCache;

//     @Autowired
//     private HttpServletRequest request;

//     public LoginAttemptService() {
//         super();
//         attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
//             @Override
//             public Integer load(final String key) {
//                 return 0;
//             }
//         });
//     }

//     public void loginFailed(final String key) {
//         int attempts;
//         try {
//             attempts = attemptsCache.get(key);
//         } catch (final ExecutionException e) {
//             attempts = 0;
//         }
//         attempts++;
//         attemptsCache.put(key, attempts);
//     }

//     public boolean isBlocked() {
//         try {
//             return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
//         } catch (final ExecutionException e) {
//             return false;
//         }
//     }

//     private String getClientIP() {
//         final String xfHeader = request.getHeader("X-Forwarded-For");
//         if (xfHeader != null) {
//             return xfHeader.split(",")[0];
//         }
//         return request.getRemoteAddr();
//     }
// }
