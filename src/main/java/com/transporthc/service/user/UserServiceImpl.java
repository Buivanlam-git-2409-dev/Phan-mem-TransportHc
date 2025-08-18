package com.transporthc.service.user;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.UserEntity;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.user.UserMapper;
import com.transporthc.repository.user.UserRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import com.transporthc.utils.FileFactory;
import com.transporthc.utils.ImportConfig;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends BaseService implements UserService{
    private final EntityManager entityManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.USERS;

    @Override
    public UserEntity createUser(UserDto userDto) {
        UserEntity user = userMapper.toUser(userDto);
        return userRepo.save(user);
    }

    @Override
    public UserEntity updateUser(String id, UserDto updateUserDTO) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        UserEntity existingUser = userRepo.getUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }


        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        entityManager.clear();
        if (!userRepo.updateUser(existingUser, id, updateUserDTO)) {
            throw new RuntimeException("Failed to update user with ID: " + id);
        }

        return userRepo.getUserById(id);
    }

    @Override
    public List<UserEntity> getAllUsers(int page) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return userRepo.getAll(page);
    }

    @Override
    public UserEntity getUserById(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        UserEntity user = userRepo.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        return user;
    }

    @Override
    public String deleteById(String id) {
        checkPermission(type, PermissionKeyEnum.DELETE);
        return (userRepo.deleteUser(id) > 0 ? "Deleted" : "failure");
    }

    @Override
    public List<UserDto> getDriver(int page) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return userRepo.getDriver(page);
    }

    @Override
    public List<UserDto> getAdmin(int page) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return userRepo.getAdmin(page);
    }

    @Override
    public List<UserEntity> importUserData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<UserDto> userDTOList = ExcelUtils.getImportData(workbook, ImportConfig.userImport);

        List<UserEntity> users = userMapper.toUserList(userDTOList);
        return userRepo.saveAll(users);
    }

    @Override
    public ExportExcelResponse exportDriver(int page) throws Exception {
        List<UserDto> users = userRepo.getDriver(page);

        if (CollectionUtils.isEmpty(users)) {
            throw new Exception("No data");
        }
        String fileName = "Driver Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportAdmin(int page) throws Exception {
        List<UserDto> users = userRepo.getAdmin(page);

        if (!CollectionUtils.isEmpty(users)) {
            throw new Exception("No data");
        }
        String fileName = "Admin Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
