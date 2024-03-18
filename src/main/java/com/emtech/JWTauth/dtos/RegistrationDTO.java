package com.emtech.JWTauth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private String email;
    private String password;
    private String role;
    private  String lastName;private  String firstName;private  String phoneNumber;
}
