package com.example.exemployee.dto;

public class ApiResponse {
    public String code;
    public String msg;
    
    public ApiResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}