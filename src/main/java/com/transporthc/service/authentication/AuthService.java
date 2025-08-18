package com.transporthc.service.authentication;

import com.transporthc.dto.authentication.AuthRequest;
import com.transporthc.dto.authentication.AuthResponse;
import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.UserEntity;

public interface AuthService {
    UserEntity register(UserDto userDto);
    AuthResponse login(AuthRequest authRequest);
}
