package com.example.exemployee.dto;

public class LoginResponse {
    public String message;
    public String token;
    public UserInfo userInfo;
    
    public LoginResponse(String message, String token, UserInfo userInfo) {
        this.message = message;
        this.token = token;
        this.userInfo = userInfo;
    }
    
    public static class UserInfo {
        public String id;
        public String firstName;
        public String lastName;
        public String email;
        public String empIdArgano;
        public boolean emailVerified;
        
        public UserInfo(String id, String firstName, String lastName, String email, String empIdArgano, boolean emailVerified) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.empIdArgano = empIdArgano;
            this.emailVerified = emailVerified;
        }
    }
}