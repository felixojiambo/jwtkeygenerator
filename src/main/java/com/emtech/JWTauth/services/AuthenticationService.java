package com.emtech.JWTauth.services;

import com.emtech.JWTauth.dtos.AuthenticationResponse;
import com.emtech.JWTauth.dtos.AuthenticationResponse.Entity;
import com.emtech.JWTauth.dtos.RegistrationDTO;
import com.emtech.JWTauth.exceptions.UserNotFoundException;
import com.emtech.JWTauth.models.Role;
import com.emtech.JWTauth.models.Token;
import com.emtech.JWTauth.models.User;
import com.emtech.JWTauth.repository.TokenRepository;
import com.emtech.JWTauth.repository.UserRepository;
import com.emtech.JWTauth.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }


//
//
//    public AuthenticationResponse register(User request) {
//        // Check if the user already exists
//        Optional<User> existingUser = repository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            // If the user exists, return a message indicating that the user is already in the system
//            return new AuthenticationResponse("User already exists in the system", null, 400);
//        }
//
//        // If the user does not exist, proceed with the registration process
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode the password
//        user.setRole(request.getRole());
//
//        user = repository.save(user); // Save the user to the database
//
//        // Optionally, generate a JWT token for the newly registered user
//        String jwt = jwtService.generateToken(user);
//
//        // Construct the Entity object for the AuthenticationResponse
//        // Note: Ensure the role is converted to a string if it's an enum
//        AuthenticationResponse.Entity entity = new AuthenticationResponse.Entity(
//                user.getId(),
//                user.getEmail(),
//                user.getRole().name(), // Convert the role enum to a string
//                jwt,
//                "Bearer" // Assuming "Bearer" is the token type
//        );
//
//        // Return the AuthenticationResponse with the message, entity, and statusCode
//        return new AuthenticationResponse("Registration successful", entity, 200);
////    }
public AuthenticationResponse register(RegistrationDTO request) {
    // Check if the user already exists
    Optional<User> existingUser = repository.findByEmail(request.getEmail());
    if (existingUser.isPresent()) {
        // If the user exists, return a message indicating that the user is already in the system
        return new AuthenticationResponse("User already exists in the system", null, 400);
    }

    // If the user does not exist, proceed with the registration process
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode the password
    user.setRole(Role.valueOf(request.getRole()));
    user.setLastName(request.getLastName()); // Set the lastName
    user.setFirstName(request.getFirstName()); // Set the firstName
    user.setPhoneNumber(request.getPhoneNumber()); // Set the phoneNumber

    user = repository.save(user); // Save the user to the database

    // Generate a JWT token for the newly registered user
    String jwt = jwtService.generateToken(user); // Ensure jwtService is available and correctly implemented
    String bearer = "Bearer"; // The type of the token, typically "Bearer"

    // Use the fromUser method to create an AuthenticationResponse with the Entity
    return AuthenticationResponse.fromUser(user, jwt, bearer);
}


    public AuthenticationResponse authenticate(User request) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Find the user by email
        User user = repository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        // Generate a JWT token for the user
        String jwt = jwtService.generateToken(user);

        // Revoke all tokens by the user
        revokeAllTokenByUser(user);

        // Save the new token
        saveUserToken(jwt, user);

        // Extract type, bearer, and role from the JWT and user object
        String type = "Bearer"; // Typically "Bearer" for JWT tokens
        String bearer = type; // The bearer token is usually the same as the type
        String role = String.valueOf(user.getRole()); // Assuming the user object has a getRole() method

        // Construct the Entity object
        AuthenticationResponse.Entity entity = new AuthenticationResponse.Entity(
                user.getId(),
                user.getEmail(),
                role,
                jwt,
                type
        );

        // Return the AuthenticationResponse with the message, entity, and statusCode
        return new AuthenticationResponse("Authentication successful", entity, 200);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
