//package com.emtech.JWTauth.services;
//
//import com.emtech.JWTauth.dtos.AuthenticationResponse;
//import com.emtech.JWTauth.models.Token;
//import com.emtech.JWTauth.models.User;
//import com.emtech.JWTauth.repository.TokenRepository;
//import com.emtech.JWTauth.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AuthenticationService {
//    @Autowired
//    private final UserRepository repository;
//   @Autowired
//    private final PasswordEncoder passwordEncoder;
//  @Autowired
//    private final JwtService jwtService;
//@Autowired
//    private final TokenRepository tokenRepository;
//@Autowired
//    private final AuthenticationManager authenticationManager;
//
//    public AuthenticationService(UserRepository repository,
//                                 PasswordEncoder passwordEncoder,
//                                 JwtService jwtService,
//                                 TokenRepository tokenRepository,
//                                 AuthenticationManager authenticationManager) {
//        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//        this.tokenRepository = tokenRepository;
//        this.authenticationManager = authenticationManager;
//    }
//
//    public AuthenticationResponse register(User request) {
//
//        // check if user already exist. if exist than authenticate the user
//        if(repository.findByEmail(request.getEmail()).isPresent()) {
//            return new AuthenticationResponse(null, "User already exist");
//        }
//
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//
//        user.setRole(request.getRole());
//
//        user = repository.save(user);
//
//        String jwt = jwtService.generateToken(user);
//
//        saveUserToken(jwt, user);
//
//        return new AuthenticationResponse(jwt, "User registration was successful");
//
//    }
//
//    public AuthenticationResponse authenticate(User request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//
//        User user = repository.findByEmail(request.getEmail()).orElseThrow();
//        String jwt = jwtService.generateToken(user);
//
//        revokeAllTokenByUser(user);
//        saveUserToken(jwt, user);
//
//        return new AuthenticationResponse(jwt, "User login was successful");
//
//    }
//    private void revokeAllTokenByUser(User user) {
//        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
//        if(validTokens.isEmpty()) {
//            return;
//        }
//
//        validTokens.forEach(t-> {
//            t.setLoggedOut(true);
//        });
//
//        tokenRepository.saveAll(validTokens);
//    }
//    private void saveUserToken(String jwt, User user) {
//        Token token = new Token();
//        token.setToken(jwt);
//        token.setLoggedOut(false);
//        token.setUser(user);
//        tokenRepository.save(token);
//    }
//}
package com.emtech.JWTauth.services;

import com.emtech.JWTauth.dtos.AuthenticationResponse;
import com.emtech.JWTauth.models.Role;
import com.emtech.JWTauth.models.Token;
import com.emtech.JWTauth.models.User;
import com.emtech.JWTauth.repository.TokenRepository;
import com.emtech.JWTauth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AuthenticationResponse register(User request) {
        // Check if the user already exists
        Optional<User> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            // If the user exists, return a message indicating that the user is already in the system
            return new AuthenticationResponse( "User already exists in the system");
        }

        // If the user does not exist, proceed with the registration process
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = repository.save(user);

        // Generate a JWT token for the newly registered user
        String jwt = jwtService.generateToken(user);

        // Save the user token
        saveUserToken(jwt, user);

        // Return the JWT token in the response
        return new AuthenticationResponse(jwt);
    }


    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
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