package com.disa.auth_service.service;

import com.disa.auth_service.dto.LoginRequest;
import com.disa.auth_service.dto.LoginResponse;
import com.disa.auth_service.dto.RegisterRequest;
import com.disa.auth_service.dto.UpdateUserRequest;
import com.disa.auth_service.dto.UserProfileResponse;
import com.disa.auth_service.entity.Role;
import com.disa.auth_service.entity.User;
import com.disa.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /**
     * Registers a new user with the provided registration details.
     * Checks for existing username and email, encodes the password,
     * saves the user, and generates a JWT token.
     *
     * @param request the registration request containing user information
     * @return LoginResponse containing the JWT token and user details
     */
    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.VOLUNTEER);
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);

        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));

        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole(), user.getFullName());
    }

    /**
     * Authenticates a user with the provided login credentials.
     * Uses the AuthenticationManager to verify credentials and generates a JWT token.
     *
     * @param request the login request containing username and password
     * @return LoginResponse containing the JWT token and user details
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole(), user.getFullName());
    }

    /**
     * Retrieves the profile information for a user by username.
     * Fetches the user from the repository and returns their profile details.
     *
     * @param username the username of the user
     * @return UserProfileResponse containing the user's profile details
     */
    @Override
    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Updates the profile information for a user.
     * Fetches the user, updates the fields, saves, and returns the updated profile.
     *
     * @param username the username of the user to update
     * @param request the update request containing new user information
     * @return UserProfileResponse containing the updated user's profile details
     */
    @Override
    public UserProfileResponse updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is being changed and if it's already taken by another user
        if (!user.getEmail().equals(request.getEmail()) &&
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : user.getRole());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Deletes a user by username.
     * Fetches the user and deletes it from the repository.
     *
     * @param username the username of the user to delete
     */
    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    /**
     * Validates a JWT token by extracting the username and checking validity.
     * Catches any exceptions during validation and returns false.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}