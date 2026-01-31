package com.disa.auth_service.controller;

import com.disa.auth_service.dto.LoginRequest;
import com.disa.auth_service.dto.LoginResponse;
import com.disa.auth_service.dto.RegisterRequest;
import com.disa.auth_service.dto.UpdateUserRequest;
import com.disa.auth_service.dto.UserProfileResponse;
import com.disa.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and User Management API")
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
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
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
    @Operation(summary = "User login", description = "Authenticates user credentials and returns a JWT token")
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
    @Operation(summary = "Validate JWT token", description = "Checks if the provided JWT token is valid")
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
    @Operation(summary = "Get user profile", description = "Retrieves the profile information for the authenticated user")
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
    @Operation(summary = "Update user profile", description = "Updates the profile information for the authenticated user")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request, Authentication authentication) {
        String username = authentication.getName();
        UserProfileResponse profile = authService.updateUser(username, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Deletes the authenticated user's account.
     *
     * @param authentication the current authentication object
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/profile")
    @Operation(summary = "Delete user account", description = "Deletes the authenticated user's account")
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String username = authentication.getName();
        authService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}