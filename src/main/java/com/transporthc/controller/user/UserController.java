package com.transporthc.controller.user;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.user.UserDto;
import com.transporthc.dto.BaseResponse;
import com.transporthc.entity.user.UserEntity;
import com.transporthc.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<UserEntity>> createUser(@Valid @RequestBody UserDto userDto) {
        UserEntity createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(BaseResponse.ok(createdUser));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BaseResponse<UserEntity>> updateUser(@PathVariable String id, @RequestBody UserDto updateUserDto) {
        UserEntity updatedUser = userService.updateUser(id, updateUserDto);
        return ResponseEntity.ok(BaseResponse.ok(updatedUser));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<UserEntity>>> getAllUsers(@RequestParam int page) {
        List<UserEntity> users = userService.getAllUsers(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<UserEntity>> getUserById(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(BaseResponse.ok(user));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<String>> deleteUserById(@PathVariable String id) {
        String notice = userService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.ok(notice));
    }

    @GetMapping("/driver")
    public ResponseEntity<BaseResponse<List<UserDto>>> getDriver(@RequestParam int page) {
        List<UserDto> users = userService.getDriver(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAdmin(@RequestParam int page) {
        List<UserDto> users = userService.getAdmin(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/export/driver")
    public ResponseEntity<Object> exportDriver(@RequestParam int page) throws Exception {

        ExportExcelResponse exportExcelResponse = userService.exportDriver(page);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @GetMapping("/export/admin")
    public ResponseEntity<Object> exportAdmin(@RequestParam int page) throws Exception {

        ExportExcelResponse exportExcelResponse = userService.exportAdmin(page);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importScheduleData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(userService.importUserData(importFile)),
                HttpStatus.CREATED
        );
    }
}