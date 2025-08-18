package com.transporthc.service.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.UserEntity;

@Service
public interface UserService {
    UserEntity createUser(UserDto userDto);
    UserEntity updateUser(String id, UserDto updateUserDTO);
    List<UserEntity> getAllUsers(int page);
    UserEntity getUserById(String id);
    String deleteById(String id);
    List<UserDto> getDriver(int page);
    List<UserDto> getAdmin(int page);
    List<UserEntity> importUserData(MultipartFile importFile);
    ExportExcelResponse exportDriver(int page) throws Exception;
    ExportExcelResponse exportAdmin(int page) throws Exception;
}
