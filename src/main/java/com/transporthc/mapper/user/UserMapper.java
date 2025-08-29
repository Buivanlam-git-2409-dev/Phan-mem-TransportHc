package com.transporthc.mapper.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.User;
import com.transporthc.enums.IDKey;
import com.transporthc.utils.utils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toUser(UserDto dto) {
        if (dto == null) { return null; }
        return User.builder()
                .id(utils.genID(IDKey.USER))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .note(dto.getNote())
                .dob(dto.getDateOfBirth())
                .imgPath(dto.getImagePath())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roleId(dto.getRoleId())
                .status(1)
                .build();
    }

    public List<User> toUserList(List<UserDto> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toUser).collect(Collectors.toList());
    }
}
