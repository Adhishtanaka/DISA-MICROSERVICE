package com.disa.auth_service.service;

import com.disa.auth_service.dto.LoginRequest;
import com.disa.auth_service.dto.LoginResponse;
import com.disa.auth_service.dto.RegisterRequest;
import com.disa.auth_service.dto.UserProfileResponse;

public interface AuthService {
    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user information
     * @return LoginResponse containing the JWT token and user details
     */
    LoginResponse register(RegisterRequest request);

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param request the login request containing username and password
     * @return LoginResponse containing the JWT token and user details
     */
    LoginResponse login(LoginRequest request);

    /**
     * Retrieves the profile information for a user by username.
     *
     * @param username the username of the user
     * @return UserProfileResponse containing the user's profile details
     */
    UserProfileResponse getProfile(String username);

    /**
     * Validates a JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}