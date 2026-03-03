package com.smartparking.controller;

import com.smartparking.dto.request.LoginRequestDTO;
import com.smartparking.dto.request.RegisterRequestDTO;
import com.smartparking.dto.response.ApiResponseDTO;
import com.smartparking.dto.response.AuthResponseDTO;
import com.smartparking.entity.User;
import com.smartparking.repository.UserRepository;
import com.smartparking.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication
 * Handles user login and registration
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * User Login API
     * POST /api/auth/login
     * 
     * @param loginRequest - Login credentials
     * @return JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        
        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);
        
        // Get user from database to retrieve role
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create response
        AuthResponseDTO authResponse = new AuthResponseDTO(
                token,
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new ApiResponseDTO(true, "Login successful", authResponse)
        );
    }

    /**
     * User Registration API
     * POST /api/auth/register
     * 
     * @param registerRequest - User registration details
     * @return Success message
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, "Username already exists", null));
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO(true, "User registered successfully", null));
    }
}