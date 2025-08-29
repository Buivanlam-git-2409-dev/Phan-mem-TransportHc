package com.transporthc.service.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.User;

@Service
public interface UserService {
    User createUser(UserDto userDto);
    User updateUser(String id, UserDto updateUserDTO);
    List<User> getAllUsers(int page);
    User getUserById(String id);
    String deleteById(String id);
    List<UserDto> getDriver(int page);
    List<UserDto> getAdmin(int page);
    List<User> importUserData(MultipartFile importFile);
    ExportExcelResponse exportDriver(int page) throws Exception;
    ExportExcelResponse exportAdmin(int page) throws Exception;
}
