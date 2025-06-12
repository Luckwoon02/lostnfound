package com.example.demo.dto;

public record AuthenticationResponse(
    String token,
    String username,
    String email,
    String fullName
) {}
