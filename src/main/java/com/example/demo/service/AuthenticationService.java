package com.example.demo.service;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhoneNumber(request.phoneNumber());
        user.setRoles(Set.of(UserRole.ROLE_USER));
        user = userRepository.save(user);
        
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthenticationResponse(
            jwtToken,
            user.getUsername(),
            user.getEmail(),
            user.getFullName()
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );
        
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthenticationResponse(
            jwtToken,
            user.getUsername(),
            user.getEmail(),
            user.getFullName()
        );
    }
}
