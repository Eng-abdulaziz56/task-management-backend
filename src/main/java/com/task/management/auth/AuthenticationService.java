package com.task.management.auth;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.auth.dto.AuthenticationResponse;
import com.task.management.auth.dto.LoginRequest;
import com.task.management.auth.dto.RegisterRequest;
import com.task.management.auth.token.Token;
import com.task.management.auth.token.TokenRepository;
import com.task.management.auth.token.TokenType;
import com.task.management.config.JwtService;
import com.task.management.config.exception.customExceptions.CustomBadRequestException;
import com.task.management.config.exception.customExceptions.DuplicateResourceException;
import com.task.management.user.Role;
import com.task.management.user.User;
import com.task.management.user.UserRepository;
import com.task.management.user.dto.ChangePasswordRequest;
import com.task.management.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if(repository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        ResponseUtil.error("Missing or invalid Authorization header"));
                return;
            }

            final String refreshToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(refreshToken);
            if (userEmail == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        ResponseUtil.error("Invalid token"));
                return;
            }

            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!jwtService.isTokenValid(refreshToken, user)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        ResponseUtil.error("Invalid or expired refresh token"));
                return;
            }

            var accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), ResponseEntity.ok(ResponseUtil.success("success", authResponse)));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new ObjectMapper().writeValue(response.getOutputStream(),
                    ResponseUtil.error("Server error: " + e.getMessage()));
        }
    }

    public void changePassword(String username, ChangePasswordRequest request ) {

        User user = repository.findByEmail(username).orElseThrow(() -> new CustomBadRequestException("User not found"));

        if(!passwordEncoder.matches(user.getPassword(), request.getOldPassword())){
            throw new CustomBadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

}