package com.transporthc.repository.user;

import java.util.List;

import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.User;

public interface UserRepoCustom {
    List<User> getAll(int page);
    User getUserById(String id);
    long deleteUser(String id);
    User findByUsername(String username);
    List<UserDto> getDriver(int page);
    List<UserDto> getAdmin(int page);
    Boolean updateUser(User exitingUser,String id, UserDto updateUserDTO);
}
