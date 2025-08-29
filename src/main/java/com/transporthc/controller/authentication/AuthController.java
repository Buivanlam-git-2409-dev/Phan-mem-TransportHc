package com.transporthc.controller.authentication;

import com.transporthc.dto.authentication.AuthRequest;
import com.transporthc.dto.user.UserDto;
import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.authentication.AuthResponse;
import com.transporthc.entity.user.User;
import com.transporthc.service.authentication.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<User>> registerUser(@Valid @RequestBody UserDto userDto) {
        User createdUser = authService.register(userDto);
        return ResponseEntity.ok(BaseResponse.ok(createdUser));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(BaseResponse.ok(authResponse));
    }
}