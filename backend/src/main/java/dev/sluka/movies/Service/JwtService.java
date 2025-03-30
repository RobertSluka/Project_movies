package dev.sluka.movies.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.sluka.movies.DTO.UserDTO;
import dev.sluka.movies.Entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    // private final Dotenv dotenv = Dotenv.load();
    @Value("${jwt.secret}")
    private String secretKey ;

    public String generateToken(UserDTO userDto) {
        Map<String, Object> claims
                = new HashMap<>();

                Date issuedAt = new Date(System.currentTimeMillis());
                Date expiration = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24);

                System.out.println("Generating token for: " + userDto.getUserName());
                System.out.println("Issued at: " + issuedAt);
                System.out.println("Expires at: " + expiration);
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(userDto.getUserName())
                .issuer("DCB")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .and()
                .signWith(generateKey())
                .compact();
    }
    // public String generateRefreshToken(UserDTO userDto) {
    //     Map<String, Object> claims = new HashMap<>();
    
    //     return Jwts
    //             .builder()
    //             .claims()
    //             .add(claims)
    //             .subject(userDto.getUserName())
    //             .issuer("DCB")
    //             .issuedAt(new Date(System.currentTimeMillis()))
    //             .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
    //             .and()
    //             .signWith(generateKey())
    //             .compact();
    // }
    private SecretKey generateKey() {
        byte[] decode
                = Decoders.BASE64.decode(getSecretKey());

        return Keys.hmacShaKeyFor(decode);
    }


    public String getSecretKey() {
        return secretKey ;
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        try {
        System.out.println("🔍 Parsing token in extractClaims(): " + token);
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    } catch (ExpiredJwtException e) {
        System.out.println("❌ Token expired at: " + e.getClaims().getExpiration());
        System.out.println("❌ Offending token: " + e.getClaims());
        throw e;
    }
}

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}