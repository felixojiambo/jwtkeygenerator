package com.emtech.JWTauth.dtos;
import com.emtech.JWTauth.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String message; // The message from the server
    private AuthenticationResponse.Entity entity; // The entity containing user details
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

        public Entity(Integer id, String email, Role role, String jwt, String bearer) {
        }
    }
}
