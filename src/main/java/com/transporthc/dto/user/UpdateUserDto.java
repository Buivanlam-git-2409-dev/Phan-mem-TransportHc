package com.transporthc.dto.user;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    private String fullName;
    private String phone;
    private Date dateOfBirth;
    private String imagePath;
    private String note;
    private String username;
    private String password;
    private Integer roleId;
    private Integer status;
}
