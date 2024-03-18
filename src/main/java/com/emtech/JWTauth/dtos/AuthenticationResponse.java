package com.emtech.JWTauth.dtos;

import com.emtech.JWTauth.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String message; // The message from the server
    private Entity entity; // The entity containing user details
    private int statusCode; // The status code of the response

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entity {
        private int id; // The user's ID
        private String email; // The user's email
        private String role; // The user's role
        private String access_token; // The access token
        private String tokenType; // The type of the token, typically "Bearer"
    }

    // Static factory method to create an AuthenticationResponse from a User object
    public static AuthenticationResponse fromUser(User user, String jwt, String bearer) {
        Entity entity = new Entity(
                user.getId(),
                user.getEmail(),
                user.getRole().name(), // Convert the role enum to a string
                jwt,
                bearer
        );
        return new AuthenticationResponse("Authentication successful", entity, 200);
    }
}
