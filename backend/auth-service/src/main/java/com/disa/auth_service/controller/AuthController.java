package com.disa.auth_service.controller;

import com.disa.auth_service.dto.LoginRequest;
import com.disa.auth_service.dto.LoginResponse;
import com.disa.auth_service.dto.RegisterRequest;
import com.disa.auth_service.dto.UserProfileResponse;
import com.disa.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Handles user registration requests.
     * Validates the registration request and creates a new user account.
     *
     * @param request the registration request containing user details
     * @return ResponseEntity containing the login response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user login requests.
     * Authenticates the user credentials and returns a JWT token upon success.
     *
     * @param request the login request containing username and password
     * @return ResponseEntity containing the login response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Validates a JWT token.
     * Extracts the token from the Authorization header and checks its validity.
     *
     * @param token the Authorization header containing the Bearer token
     * @return ResponseEntity containing true if token is valid, false otherwise
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Retrieves the user profile for the authenticated user.
     * Uses the authentication object to get the username and fetch profile details.
     *
     * @param authentication the current authentication object
     * @return ResponseEntity containing the user profile response
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileResponse profile = authService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    /**
     * Updates the user profile for the authenticated user.
     * Currently returns the current profile for simplicity; in a real application,
     * this would implement update logic based on the request.
     *
     * @param request the update request containing new profile details
     * @param authentication the current authentication object
     * @return ResponseEntity containing the updated user profile response
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody RegisterRequest request, Authentication authentication) {
        // For simplicity, just return current profile. In real app, update logic here.
        String username = authentication.getName();
        UserProfileResponse profile = authService.getProfile(username);
        return ResponseEntity.ok(profile);
    }
}