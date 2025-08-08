package com.transporthc.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.transporthc.annotations.ExportColumn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	@ExportColumn(name = "Mã NV")
    private String id;

    @NotBlank(message = "Tên không được để trống")
    @ExportColumn(name = "Họ tên")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0(?!0)\\d{9}$", message = "Số điện thoại là dãy 10 chữ số và bắt đầu bằng 0")
    @ExportColumn(name = "Số ĐT")
    private String phone;

    @NotNull(message = "Ngày sinh không được để trống")
    @ExportColumn(name = "Ngày sinh")
    private Date dateOfBirth;

    private String imagePath;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự.")
    private String password;

    @NotNull(message = "Role ID không được để trống.")
    private Integer roleId;
    @ExportColumn(name = "Loại tài khoản")
    private String roleName;

    private Integer status;
    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
}
