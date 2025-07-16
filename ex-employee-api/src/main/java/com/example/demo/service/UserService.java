package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        Optional<UserEntity> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (user.getOtp().equals(otp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                user.setEmailVerified(true);
                user.setOtp(null);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }
}

