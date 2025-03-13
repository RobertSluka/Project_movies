package dev.sluka.movies.config;

import java.util.List;

import java.util.Collections;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.IOException;
import java.util.Arrays;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserDataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, java.io.IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Capture IP Address
        String ipAddress = req.getRemoteAddr();

        // Capture User-Agent
        String userAgent = req.getHeader("User-Agent");

        // Capture Language
        String language = req.getHeader("Accept-Language");

        // Capture Cookies
        Cookie[] cookies = req.getCookies();
        List<String> cookieValues = cookies != null ? Arrays.stream(cookies).map(Cookie::getValue).toList() : Collections.emptyList();

        // Log or store user data
        System.out.println("User IP: " + ipAddress);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Language: " + language);
        System.out.println("Cookies: " + cookieValues);

        // Proceed with the request
        chain.doFilter(request, response);
    }
}
