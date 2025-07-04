package com.task.management.user;

import com.task.management.user.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }
}