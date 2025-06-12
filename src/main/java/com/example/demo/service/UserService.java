package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.AuthProvider;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void processOAuthPostLogin(String email, String name) {
        // Check if user exists
        Optional<User> existUser = userRepository.findByEmail(email);
        
        if (existUser.isEmpty()) {
            // Create a set with USER role
            Set<UserRole> roles = new HashSet<>();
            roles.add(UserRole.ROLE_USER);
            
            // Create new user
            User newUser = User.builder()
                .email(email)
                .username(generateUniqueUsername(email))
                .fullName(name)
                .password(passwordEncoder.encode("OAUTH2_" + email + "_PASSWORD"))
                .provider(AuthProvider.google)
                .providerId(email) // Using email as provider ID for simplicity
                .roles(roles)
                .build();
            
            userRepository.save(newUser);
        }
    }
    
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;
        
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
