package com.transporthc.service.authentication;

import com.transporthc.dto.authentication.AuthRequest;
import com.transporthc.dto.user.UserDto;
import com.transporthc.dto.authentication.AuthResponse;
import com.transporthc.entity.user.UserEntity;
import com.transporthc.exception.define.ConflictException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.user.UserMapper;
import com.transporthc.repository.user.UserRepo;
import com.transporthc.service.JwtService;
import com.transporthc.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    public AuthServiceImpl(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper, UserRepo userRepo) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserEntity register(UserDto userDto) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userRepo.findByUsername(userDto.getUsername()) != null) {
            throw new ConflictException("Tên đăng nhập đã tồn tại.");
        }

        return userService.createUser(userDto);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Sai tên đăng nhập hoặc mật khẩu.");
        }

        UserEntity user = userRepo.findByUsername(authRequest.getUsername());
        if (user == null) {
            throw new NotFoundException("Không tìm thấy người dùng.");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}
