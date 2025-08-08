package com.example.exemployee.dto;

public class LoginApiResponse {
    public String code;
    public String msg;
    public String token;
    public LoginResponse.UserInfo userInfo;
    
    public LoginApiResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public LoginApiResponse(String code, String msg, String token, LoginResponse.UserInfo userInfo) {
        this.code = code;
        this.msg = msg;
        this.token = token;
        this.userInfo = userInfo;
    }
}