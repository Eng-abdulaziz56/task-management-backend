package com.task.management.user;


import com.task.management.dto.ApiResponse;
import com.task.management.user.dto.ChangePasswordRequest;
import com.task.management.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping(path = "/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @Valid  @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseUtil.success("Password changed successfully", null);
    }

}