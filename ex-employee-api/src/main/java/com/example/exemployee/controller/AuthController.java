package com.example.exemployee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.exemployee.dto.ApiResponse;
import com.example.exemployee.dto.LoginApiResponse;
import com.example.exemployee.dto.LoginRequest;
import com.example.exemployee.dto.LoginResponse;
import com.example.exemployee.dto.ResendOtpRequest;
import com.example.exemployee.dto.SignupRequest;
import com.example.exemployee.dto.VerifyOtpRequest;
import com.example.exemployee.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest request) {
        ApiResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        ApiResponse response = userService.verifyOtp(request.email, request.otp);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody ResendOtpRequest request) {
        ApiResponse response = userService.resendOtp(request.email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginRequest request) {
        LoginApiResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
