package com.emtech.JWTauth.dtos;


public class AuthenticationResponse {
//    private String token;
//    private String message;
//
//    public AuthenticationResponse(String token, String message) {
//        this.token = token;
//        this.message = message;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public String getMessage() {
//        return message;
//    }
private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}