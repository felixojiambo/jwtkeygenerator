package com.emtech.JWTauth.config;

import com.emtech.JWTauth.models.Role;
import com.emtech.JWTauth.models.Token;
import com.emtech.JWTauth.models.User;
import com.emtech.JWTauth.repository.TokenRepository;
import com.emtech.JWTauth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.List;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

//    @Override
//    public void logout(HttpServletRequest request,
//                       HttpServletResponse response,
//                       Authentication authentication) {
//        String authHeader = request.getHeader("Authorization");
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        Token storedToken = tokenRepository.findByToken(token).orElse(null);
//
//        if(storedToken != null) {
//            storedToken.setLoggedOut(true);
//            tokenRepository.save(storedToken);
//        }
//    }
@Override
public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        // Log a message if the Authorization header is missing or incorrectly formatted
        System.out.println("Invalid or missing Authorization header.");
        return;
    }

    String token = authHeader.substring(7);
    Token storedToken = tokenRepository.findByToken(token).orElse(null);

    if (storedToken != null) {
        storedToken.setLoggedOut(true);
        tokenRepository.save(storedToken);
        // Log a message indicating that the token has been invalidated
        System.out.println("Token invalidated: " + token);
    } else {
        // Log a message if the token is not found in the database
        System.out.println("Token not found in database: " + token);
    }

    // Clear the security context
    SecurityContextHolder.clearContext();
}

}