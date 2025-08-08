package com.example.exemployee.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exemployee.dto.ApiResponse;
import com.example.exemployee.dto.LoginApiResponse;
import com.example.exemployee.dto.LoginRequest;
import com.example.exemployee.dto.LoginResponse;
import com.example.exemployee.dto.ResendOtpRequest;
import com.example.exemployee.dto.SignupRequest;
import com.example.exemployee.entity.UserEntity;
import com.example.exemployee.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OTPService otpService;

    public ApiResponse register(SignupRequest request) {
        // Check if user already exists using findAllByEmail to handle duplicates
        List<UserEntity> existingUsers = userRepo.findAllByEmail(request.email);
        
        if (!existingUsers.isEmpty()) {
            // Find if there's a verified user
            boolean hasVerifiedUser = existingUsers.stream().anyMatch(UserEntity::isEmailVerified);
            
            if (hasVerifiedUser) {
                return new ApiResponse("USER_EXISTS", "User with this email already exists and is verified");
            }
            
            // If no verified user, update the first unverified user and clean up others
            UserEntity user = existingUsers.get(0);
            
            // Update user details with new signup data
            user.setFirstName(request.firstName);
            user.setLastName(request.lastName);
            user.setPhoneNumber(request.phoneNumber);
            user.setEmpIdArgano(request.empIdArgano);
            user.setPassword(new BCryptPasswordEncoder().encode(request.password));
            
            // Generate and send new OTP
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            userRepo.save(user);
            otpService.sendOtpEmail(request.email, otp);
            
            // Clean up other duplicates
            cleanupDuplicateUsers(request.email, user.getId());
            return new ApiResponse("SUCCESS", "Register successful. OTP sent to your email.");
        }

        // Create new user
        UserEntity user = new UserEntity();
        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        user.setPhoneNumber(request.phoneNumber);
        user.setEmail(request.email);
        user.setEmpIdArgano(request.empIdArgano);
        user.setPassword(new BCryptPasswordEncoder().encode(request.password));

        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        user.setEmailVerified(false);

        userRepo.save(user);
        otpService.sendOtpEmail(request.email, otp);
        
        return new ApiResponse("SUCCESS", "Register successful. OTP sent to your email.");
    }

    public ApiResponse verifyOtp(String email, String otp) {
        // Use findAllByEmail to handle duplicates gracefully
        List<UserEntity> users = userRepo.findAllByEmail(email);
        
        if (users.isEmpty()) {
            return new ApiResponse("FAILED", "Unable to verify OTP. Please resend OTP.");
        }
        
        // Find user with matching OTP
        for (UserEntity user : users) {
            if (user.getOtp() != null && user.getOtp().equals(otp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                user.setEmailVerified(true);
                user.setOtp(null);
                userRepo.save(user);
                
                // Clean up other duplicate unverified users
                cleanupDuplicateUsers(email, user.getId());
                return new ApiResponse("SUCCESS", "OTP Verified.");
            }
        }
        
        return new ApiResponse("FAILED", "Unable to verify OTP. Please resend OTP.");
    }
    
    public ApiResponse resendOtp(String email) {
        // Find user by email
        List<UserEntity> users = userRepo.findAllByEmail(email);
        
        if (users.isEmpty()) {
            return new ApiResponse("FAILED", "User not found with this email address.");
        }
        
        // Check if there's a verified user
        UserEntity verifiedUser = users.stream()
            .filter(UserEntity::isEmailVerified)
            .findFirst()
            .orElse(null);
            
        if (verifiedUser != null) {
            return new ApiResponse("USER_VERIFIED", "User with this email is already verified.");
        }
        
        // Find unverified user and send OTP
        UserEntity user = users.stream()
            .filter(u -> !u.isEmailVerified())
            .findFirst()
            .orElse(null);
            
        if (user == null) {
            return new ApiResponse("FAILED", "User not found with this email address.");
        }
        
        // Generate new OTP and update user
        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);
        
        // Send OTP email
        otpService.sendOtpEmail(email, otp);
        
        return new ApiResponse("SUCCESS", "OTP sent successfully.");
    }
    
    private void cleanupDuplicateUsers(String email, String keepUserId) {
        List<UserEntity> users = userRepo.findAllByEmail(email);
        for (UserEntity user : users) {
            if (!user.getId().equals(keepUserId) && !user.isEmailVerified()) {
                userRepo.delete(user);
            }
        }
    }
    
    public LoginApiResponse login(LoginRequest request) {
        List<UserEntity> users = userRepo.findAllByEmail(request.email);
        
        if (users.isEmpty()) {
            return new LoginApiResponse("FAILED", "User not found with this email address.");
        }
        
        // Find verified user
        UserEntity user = users.stream()
            .filter(UserEntity::isEmailVerified)
            .findFirst()
            .orElse(null);
            
        if (user == null) {
            return new LoginApiResponse("EMAIL_NOT_VERIFIED", "Email not verified. Please verify your email first.");
        }
        
        // Check password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.password, user.getPassword())) {
            return new LoginApiResponse("INVALID_CREDENTIALS", "Invalid email or password.");
        }
        
        // Generate simple token (you can replace with JWT later)
        String token = generateSimpleToken(user);
        
        // Create user info
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getEmpIdArgano(),
            user.isEmailVerified()
        );
        
        return new LoginApiResponse("SUCCESS", "Login successful.", token, userInfo);
    }
    
    private String generateSimpleToken(UserEntity user) {
        // Simple token generation - replace with JWT for production
        return "TOKEN_" + user.getId() + "_" + System.currentTimeMillis();
    }
}

