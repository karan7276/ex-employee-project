package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OTPService otpService;

    public void register(SignupRequest request) {
        // Check if user already exists using findAllByEmail to handle duplicates
        List<UserEntity> existingUsers = userRepo.findAllByEmail(request.email);
        
        if (!existingUsers.isEmpty()) {
            // Find if there's a verified user
            boolean hasVerifiedUser = existingUsers.stream().anyMatch(UserEntity::isEmailVerified);
            
            if (hasVerifiedUser) {
                throw new RuntimeException("User with this email already exists and is verified");
            }
            
            // If no verified user, update the first unverified user and clean up others
            UserEntity user = existingUsers.get(0);
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            userRepo.save(user);
            otpService.sendOtpEmail(request.email, otp);
            
            // Clean up other duplicates
            cleanupDuplicateUsers(request.email, user.getId());
            return;
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
    }

    public boolean verifyOtp(String email, String otp) {
        // Use findAllByEmail to handle duplicates gracefully
        List<UserEntity> users = userRepo.findAllByEmail(email);
        
        if (users.isEmpty()) {
            return false;
        }
        
        // Find user with matching OTP
        for (UserEntity user : users) {
            if (user.getOtp() != null && user.getOtp().equals(otp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                user.setEmailVerified(true);
                user.setOtp(null);
                userRepo.save(user);
                
                // Clean up other duplicate unverified users
                cleanupDuplicateUsers(email, user.getId());
                return true;
            }
        }
        
        return false;
    }
    
    private void cleanupDuplicateUsers(String email, String keepUserId) {
        List<UserEntity> users = userRepo.findAllByEmail(email);
        for (UserEntity user : users) {
            if (!user.getId().equals(keepUserId) && !user.isEmailVerified()) {
                userRepo.delete(user);
            }
        }
    }
    
    public LoginResponse login(LoginRequest request) {
        List<UserEntity> users = userRepo.findAllByEmail(request.email);
        
        if (users.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        // Find verified user
        UserEntity user = users.stream()
            .filter(UserEntity::isEmailVerified)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Email not verified. Please verify your email first."));
        
        // Check password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
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
        
        return new LoginResponse("Login successful", token, userInfo);
    }
    
    private String generateSimpleToken(UserEntity user) {
        // Simple token generation - replace with JWT for production
        return "TOKEN_" + user.getId() + "_" + System.currentTimeMillis();
    }
}

