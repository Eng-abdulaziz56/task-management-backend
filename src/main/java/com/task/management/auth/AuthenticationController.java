package com.task.management.auth;

import com.task.management.auth.dto.AuthenticationResponse;
import com.task.management.auth.dto.LoginRequest;
import com.task.management.auth.dto.RegisterRequest;
import com.task.management.dto.ApiResponse;
import com.task.management.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = service.register(request);
        return (ResponseUtil.success("Account created successful", response));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid  @RequestBody LoginRequest request
    ) {
            AuthenticationResponse response = service.login(request);
            return (ResponseUtil.success("success", response));
    }



    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}