package com.transporthc.repository.user;

import java.util.List;

import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.UserEntity;

public interface UserRepoCustom {
    List<UserEntity> getAll(int page);
    UserEntity getUserById(String id);
    long deleteUser(String id);
    UserEntity findByUsername(String username);
    List<UserDto> getDriver(int page);
    List<UserDto> getAdmin(int page);
    Boolean updateUser(UserEntity exitingUser,String id, UserDto updateUserDTO);
}
